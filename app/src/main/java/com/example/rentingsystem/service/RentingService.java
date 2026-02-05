package com.example.rentingsystem.service;

import android.content.Context;
import com.example.rentingsystem.data.RentingDao;
import com.example.rentingsystem.model.Contract;
import com.example.rentingsystem.model.House;
import com.example.rentingsystem.model.RentApplication;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class RentingService {
    private RentingDao dao;

    public RentingService(Context context) {
        dao = new RentingDao(context);
    }

    // 1. Approve Application & Generate Contract
    public void approveApplication(int applicationId) {
        RentApplication app = dao.getApplication(applicationId);
        if (app != null && app.getStatus() == 1) { // Pending
            // Update Application Status
            dao.updateApplicationStatus(applicationId, 2, null); // 2=Passed

            // Generate Contract
            House house = dao.getHouse(app.getHouseId());
            if (house != null) {
                Contract contract = new Contract();
                contract.setTenantId(app.getTenantId());
                contract.setLandlordId(house.getLandlordId());
                contract.setHouseId(house.getHouseId());
                contract.setRent(house.getRent());
                contract.setStatus(1); // 1=Signing
                
                // Set Dates (e.g., Start tomorrow, End 1 year later)
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Calendar cal = Calendar.getInstance();
                cal.add(Calendar.DAY_OF_YEAR, 1);
                contract.setStartDate(sdf.format(cal.getTime()));
                cal.add(Calendar.YEAR, 1);
                contract.setEndDate(sdf.format(cal.getTime()));

                long contractId = dao.createContract(contract);
                
                // Send Notifications (Using 0 as system sender for example, or actual landlord ID)
                String content = "Contract generated. Please sign.";
                dao.createMessage(house.getLandlordId(), app.getTenantId(), content); 
                dao.createMessage(house.getLandlordId(), house.getLandlordId(), "Contract generated for House " + house.getTitle());
            }
        }
    }

    // 2. Landlord Signs Contract
    public void signContract(int contractId) {
        Contract contract = dao.getContract(contractId);
        if (contract != null && contract.getStatus() == 1) {
            // Update Contract
            dao.updateContractStatus(contractId, 2); // 2=Active
            
            // Update House
            dao.updateHouseStatus(contract.getHouseId(), 2, contractId); // 2=Rented
            
            // Notifications
            dao.createMessage(contract.getLandlordId(), contract.getTenantId(), "Contract is now Active.");
        }
    }

    // 3. Terminate Contract (Active -> Terminated)
    public void terminateContract(int contractId) {
        Contract contract = dao.getContract(contractId);
        if (contract != null && contract.getStatus() == 2) {
            // Update Contract
            dao.updateContractStatus(contractId, 4); // 4=Terminated
            
            // Update House
            dao.updateHouseStatus(contract.getHouseId(), 1, null); // 1=Rentable, Clear Contract ID
            
            // Notifications
            dao.createMessage(contract.getLandlordId(), contract.getTenantId(), "Contract has been terminated.");
        }
    }
    
    // 4. Cancel Contract (Signing -> Invalid)
    public void cancelContract(int contractId, int operatorId) {
        Contract contract = dao.getContract(contractId);
        if (contract != null && contract.getStatus() == 1) {
            dao.updateContractStatus(contractId, 5); // 5=Invalid
            // House status remains unchanged (1=Rentable)
            
            // Notify other party
            int receiverId = (operatorId == contract.getTenantId()) ? contract.getLandlordId() : contract.getTenantId();
            dao.createMessage(operatorId, receiverId, "Contract cancelled by other party.");
        }
    }
}
