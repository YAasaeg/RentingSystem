package com.example.rentingsystem.data;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.rentingsystem.model.Notice;
import com.example.rentingsystem.model.Reservation;
// ... (other imports)

// ...

    // Reservation Operations

    import com.example.rentingsystem.model.Contract;
import com.example.rentingsystem.model.House;
import com.example.rentingsystem.model.RentApplication;
import com.example.rentingsystem.model.User;

import java.util.ArrayList;
import java.util.List;

public class RentingDao {
    private DatabaseHelper dbHelper;

    public RentingDao(Context context) {
        dbHelper = new DatabaseHelper(context);
    }
    
    // Notice Operations
    public long createNotice(Notice notice) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("publisher_id", notice.getPublisherId());
        values.put("title", notice.getTitle());
        values.put("content", notice.getContent());
        values.put("receiver_id", notice.getReceiverId());
        values.put("is_read", 0);
        return db.insert(DatabaseHelper.TABLE_NOTICE, null, values);
    }

    @SuppressLint("Range")
    public List<Notice> getTenantNotices(int tenantId) {
        List<Notice> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Join Notice and User (Publisher)
        String query = "SELECT n.*, u.real_name as publisher_name "
                + "FROM " + DatabaseHelper.TABLE_NOTICE + " n "
                + "JOIN " + DatabaseHelper.TABLE_USER + " u ON n.publisher_id = u.user_id "
                + "WHERE n.receiver_id = ? ORDER BY n.create_time DESC";
        
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(tenantId)});
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Notice notice = new Notice();
                notice.setNoticeId(cursor.getInt(cursor.getColumnIndex("notice_id")));
                notice.setPublisherId(cursor.getInt(cursor.getColumnIndex("publisher_id")));
                notice.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                notice.setContent(cursor.getString(cursor.getColumnIndex("content")));
                notice.setReceiverId(cursor.getInt(cursor.getColumnIndex("receiver_id")));
                notice.setIsRead(cursor.getInt(cursor.getColumnIndex("is_read")));
                notice.setCreateTime(cursor.getString(cursor.getColumnIndex("create_time")));
                // Add extra info
                notice.setPublisherName(cursor.getString(cursor.getColumnIndex("publisher_name")));
                list.add(notice);
            }
            cursor.close();
        }
        return list;
    }

    // Get all tenants who have rented houses from this landlord (Active contracts)
    @SuppressLint("Range")
    public List<User> getLandlordTenants(int landlordId) {
        List<User> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Join Contract and User
        // Distinct tenants from active contracts (status=1) or all contracts?
        // Let's say all contracts for now, or just active. Active makes more sense for "Notices".
        String query = "SELECT DISTINCT u.* FROM " + DatabaseHelper.TABLE_CONTRACT + " c "
                + "JOIN " + DatabaseHelper.TABLE_USER + " u ON c.tenant_id = u.user_id "
                + "WHERE c.landlord_id = ? AND c.status = 1";
        
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(landlordId)});
        if (cursor != null) {
            while (cursor.moveToNext()) {
                User user = new User();
                user.setUserId(cursor.getInt(cursor.getColumnIndex("user_id")));
                user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
                user.setRealName(cursor.getString(cursor.getColumnIndex("real_name")));
                user.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
                list.add(user);
            }
            cursor.close();
        }
        return list;
    }

    // User Operations
    public long registerUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", user.getUsername());
        values.put("password", user.getPassword());
        values.put("real_name", user.getRealName());
        values.put("phone", user.getPhone());
        values.put("identity_type", user.getIdentityType());
        return db.insert(DatabaseHelper.TABLE_USER, null, values);
    }

    @SuppressLint("Range")
    public User login(String phone, String password) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_USER, null, "phone=? AND password=?", new String[]{phone, password}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            User user = new User();
            user.setUserId(cursor.getInt(cursor.getColumnIndex("user_id")));
            user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
            user.setRealName(cursor.getString(cursor.getColumnIndex("real_name")));
            user.setIdentityType(cursor.getInt(cursor.getColumnIndex("identity_type")));
            cursor.close();
            return user;
        }
        return null;
    }

    @SuppressLint("Range")
    public User getUser(int userId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_USER, null, "user_id=?", new String[]{String.valueOf(userId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            User user = new User();
            user.setUserId(cursor.getInt(cursor.getColumnIndex("user_id")));
            user.setUsername(cursor.getString(cursor.getColumnIndex("username")));
            user.setRealName(cursor.getString(cursor.getColumnIndex("real_name")));
            user.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
            user.setIdentityType(cursor.getInt(cursor.getColumnIndex("identity_type")));
            user.setPassword(cursor.getString(cursor.getColumnIndex("password")));
            cursor.close();
            return user;
        }
        return null;
    }
    public int updateUser(User user) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("username", user.getUsername());
        values.put("password", user.getPassword());
        values.put("real_name", user.getRealName());
        values.put("phone", user.getPhone());
        return db.update(DatabaseHelper.TABLE_USER, values,
                "user_id=?", new String[]{String.valueOf(user.getUserId())});
    }
    // Application Operations
    public long addApplication(RentApplication app) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tenant_id", app.getTenantId());
        values.put("house_id", app.getHouseId());
        values.put("apply_reason", app.getApplyReason());
        values.put("start_date", app.getStartDate());
        values.put("end_date", app.getEndDate());
        values.put("status", 1); // Pending
        return db.insert(DatabaseHelper.TABLE_APPLICATION, null, values);
    }

    @SuppressLint("Range")
    public RentApplication getApplication(int applyId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_APPLICATION, null, "apply_id=?", new String[]{String.valueOf(applyId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            RentApplication app = new RentApplication();
            app.setApplyId(cursor.getInt(cursor.getColumnIndex("apply_id")));
            app.setTenantId(cursor.getInt(cursor.getColumnIndex("tenant_id")));
            app.setHouseId(cursor.getInt(cursor.getColumnIndex("house_id")));
            app.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
            cursor.close();
            return app;
        }
        return null;
    }
    
    public int updateApplicationStatus(int applyId, int status, String reason) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", status);
        if (reason != null) values.put("refuse_reason", reason);
        return db.update(DatabaseHelper.TABLE_APPLICATION, values, "apply_id=?", new String[]{String.valueOf(applyId)});
    }

    @SuppressLint("Range")
    public List<RentApplication> getLandlordApplications(int landlordId) {
        List<RentApplication> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Join House and User(Tenant)
        // Note: RentApplication doesn't have houseTitle/tenantName fields yet, need to check RentApplication.java again or extend it/use a DTO.
        // For simplicity, let's just fetch raw data and maybe populate extra fields if RentApplication supports it or modify RentApplication.
        // Let's check RentApplication.java content again. It does NOT have extra fields.
        // I should modify RentApplication to add temporary fields for UI display.
        
        String query = "SELECT a.*, h.title as house_title, u.real_name as tenant_name, u.phone as tenant_phone "
                + "FROM " + DatabaseHelper.TABLE_APPLICATION + " a "
                + "JOIN " + DatabaseHelper.TABLE_HOUSE + " h ON a.house_id = h.house_id "
                + "JOIN " + DatabaseHelper.TABLE_USER + " u ON a.tenant_id = u.user_id "
                + "WHERE h.landlord_id = ? ORDER BY a.create_time DESC";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(landlordId)});
        if (cursor != null) {
            while (cursor.moveToNext()) {
                RentApplication app = new RentApplication();
                app.setApplyId(cursor.getInt(cursor.getColumnIndex("apply_id")));
                app.setTenantId(cursor.getInt(cursor.getColumnIndex("tenant_id")));
                app.setHouseId(cursor.getInt(cursor.getColumnIndex("house_id")));
                app.setApplyReason(cursor.getString(cursor.getColumnIndex("apply_reason")));
                app.setStartDate(cursor.getString(cursor.getColumnIndex("start_date")));
                app.setEndDate(cursor.getString(cursor.getColumnIndex("end_date")));
                app.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
                app.setCreateTime(cursor.getString(cursor.getColumnIndex("create_time")));
                // Add extra info to model
                app.setHouseTitle(cursor.getString(cursor.getColumnIndex("house_title")));
                app.setTenantName(cursor.getString(cursor.getColumnIndex("tenant_name")));
                app.setTenantPhone(cursor.getString(cursor.getColumnIndex("tenant_phone")));
                list.add(app);
            }
            cursor.close();
        }
        return list;
    }

    @SuppressLint("Range")
    public List<RentApplication> getTenantApplications(int tenantId) {
        List<RentApplication> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT a.*, h.title as house_title "
                + "FROM " + DatabaseHelper.TABLE_APPLICATION + " a "
                + "JOIN " + DatabaseHelper.TABLE_HOUSE + " h ON a.house_id = h.house_id "
                + "WHERE a.tenant_id = ? ORDER BY a.create_time DESC";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(tenantId)});
        if (cursor != null) {
            while (cursor.moveToNext()) {
                RentApplication app = new RentApplication();
                app.setApplyId(cursor.getInt(cursor.getColumnIndex("apply_id")));
                app.setTenantId(cursor.getInt(cursor.getColumnIndex("tenant_id")));
                app.setHouseId(cursor.getInt(cursor.getColumnIndex("house_id")));
                app.setApplyReason(cursor.getString(cursor.getColumnIndex("apply_reason")));
                app.setStartDate(cursor.getString(cursor.getColumnIndex("start_date")));
                app.setEndDate(cursor.getString(cursor.getColumnIndex("end_date")));
                app.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
                app.setCreateTime(cursor.getString(cursor.getColumnIndex("create_time")));
                // Add extra info to model
                app.setHouseTitle(cursor.getString(cursor.getColumnIndex("house_title")));
                list.add(app);
            }
            cursor.close();
        }
        return list;
    }
    
    // House Operations
    public long addHouse(House house) {
        // ... (existing code)
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("landlord_id", house.getLandlordId());
        values.put("title", house.getTitle());
        values.put("address", house.getAddress());
        values.put("rent", house.getRent());
        values.put("house_type", house.getHouseType());
        values.put("area", house.getArea());
        values.put("status", 1); // Default Rentable
        values.put("image_path", house.getImagePath());
        return db.insert(DatabaseHelper.TABLE_HOUSE, null, values);
    }

    @SuppressLint("Range")
    public House getHouse(int houseId) {
        // ... (existing code)
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_HOUSE, null, "house_id=?", new String[]{String.valueOf(houseId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            House house = cursorToHouse(cursor);
            cursor.close();
            return house;
        }
        return null;
    }
    
    @SuppressLint("Range")
    public List<House> getAllRentableHouses() {
        List<House> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // status = 1 (Rentable)
        Cursor cursor = db.query(DatabaseHelper.TABLE_HOUSE, null, "status=?", new String[]{"1"}, null, null, "create_time DESC");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(cursorToHouse(cursor));
            }
            cursor.close();
        }
        return list;
    }

    @SuppressLint("Range")
    public List<House> getHousesByLandlord(int landlordId) {
        List<House> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_HOUSE, null, "landlord_id=?", new String[]{String.valueOf(landlordId)}, null, null, "create_time DESC");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(cursorToHouse(cursor));
            }
            cursor.close();
        }
        return list;
    }

    @SuppressLint("Range")
    public List<House> searchRentableHouses(String keyword) {
        List<House> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = "status=? AND (title LIKE ? OR address LIKE ?)";
        String[] selectionArgs = new String[]{"1", "%" + keyword + "%", "%" + keyword + "%"};
        Cursor cursor = db.query(DatabaseHelper.TABLE_HOUSE, null, selection, selectionArgs, null, null, "create_time DESC");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(cursorToHouse(cursor));
            }
            cursor.close();
        }
        return list;
    }

    @SuppressLint("Range")
    public List<House> searchLandlordHouses(int landlordId, String keyword) {
        List<House> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selection = "landlord_id=? AND (title LIKE ? OR address LIKE ?)";
        String[] selectionArgs = new String[]{String.valueOf(landlordId), "%" + keyword + "%", "%" + keyword + "%"};
        Cursor cursor = db.query(DatabaseHelper.TABLE_HOUSE, null, selection, selectionArgs, null, null, "create_time DESC");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                list.add(cursorToHouse(cursor));
            }
            cursor.close();
        }
        return list;
    }

    @SuppressLint("Range")
    private House cursorToHouse(Cursor cursor) {
        House house = new House();
        house.setHouseId(cursor.getInt(cursor.getColumnIndex("house_id")));
        house.setLandlordId(cursor.getInt(cursor.getColumnIndex("landlord_id")));
        house.setTitle(cursor.getString(cursor.getColumnIndex("title")));
        house.setAddress(cursor.getString(cursor.getColumnIndex("address")));
        house.setRent(cursor.getDouble(cursor.getColumnIndex("rent")));
        house.setHouseType(cursor.getString(cursor.getColumnIndex("house_type")));
        house.setArea(cursor.getDouble(cursor.getColumnIndex("area")));
        house.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
        house.setImagePath(cursor.getString(cursor.getColumnIndex("image_path")));
        return house;
    }

    public int updateHouseStatus(int houseId, int status, Integer contractId) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", status);
        if (contractId != null) {
            values.put("current_contract_id", contractId);
        } else {
            values.putNull("current_contract_id");
        }
        return db.update(DatabaseHelper.TABLE_HOUSE, values, "house_id=?", new String[]{String.valueOf(houseId)});
    }

    // Contract Operations
    public long createContract(Contract contract) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tenant_id", contract.getTenantId());
        values.put("landlord_id", contract.getLandlordId());
        values.put("house_id", contract.getHouseId());
        values.put("start_date", contract.getStartDate());
        values.put("end_date", contract.getEndDate());
        values.put("rent", contract.getRent());
        values.put("status", contract.getStatus());
        return db.insert(DatabaseHelper.TABLE_CONTRACT, null, values);
    }
    
    @SuppressLint("Range")
    public Contract getContract(int contractId) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(DatabaseHelper.TABLE_CONTRACT, null, "contract_id=?", new String[]{String.valueOf(contractId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Contract c = new Contract();
            c.setContractId(contractId);
            c.setHouseId(cursor.getInt(cursor.getColumnIndex("house_id")));
            c.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
            cursor.close();
            return c;
        }
        return null;
    }

    public int updateContractStatus(int contractId, int status) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", status);
        return db.update(DatabaseHelper.TABLE_CONTRACT, values, "contract_id=?", new String[]{String.valueOf(contractId)});
    }

    @SuppressLint("Range")
    public List<Contract> getTenantContracts(int tenantId) {
        List<Contract> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT c.*, h.title as house_title "
                + "FROM " + DatabaseHelper.TABLE_CONTRACT + " c "
                + "JOIN " + DatabaseHelper.TABLE_HOUSE + " h ON c.house_id = h.house_id "
                + "WHERE c.tenant_id = ? ORDER BY c.create_time DESC";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(tenantId)});
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Contract c = cursorToContract(cursor);
                c.setHouseTitle(cursor.getString(cursor.getColumnIndex("house_title")));
                list.add(c);
            }
            cursor.close();
        }
        return list;
    }

    @SuppressLint("Range")
    public List<Contract> getLandlordContracts(int landlordId) {
        List<Contract> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT c.*, h.title as house_title, u.real_name as tenant_name "
                + "FROM " + DatabaseHelper.TABLE_CONTRACT + " c "
                + "JOIN " + DatabaseHelper.TABLE_HOUSE + " h ON c.house_id = h.house_id "
                + "JOIN " + DatabaseHelper.TABLE_USER + " u ON c.tenant_id = u.user_id "
                + "WHERE c.landlord_id = ? ORDER BY c.create_time DESC";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(landlordId)});
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Contract c = cursorToContract(cursor);
                c.setHouseTitle(cursor.getString(cursor.getColumnIndex("house_title")));
                c.setTenantName(cursor.getString(cursor.getColumnIndex("tenant_name")));
                list.add(c);
            }
            cursor.close();
        }
        return list;
    }

    @SuppressLint("Range")
    private Contract cursorToContract(Cursor cursor) {
        Contract c = new Contract();
        c.setContractId(cursor.getInt(cursor.getColumnIndex("contract_id")));
        c.setTenantId(cursor.getInt(cursor.getColumnIndex("tenant_id")));
        c.setLandlordId(cursor.getInt(cursor.getColumnIndex("landlord_id")));
        c.setHouseId(cursor.getInt(cursor.getColumnIndex("house_id")));
        c.setStartDate(cursor.getString(cursor.getColumnIndex("start_date")));
        c.setEndDate(cursor.getString(cursor.getColumnIndex("end_date")));
        c.setRent(cursor.getDouble(cursor.getColumnIndex("rent")));
        c.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
        c.setCreateTime(cursor.getString(cursor.getColumnIndex("create_time")));
        return c;
    }
    
    // Message Operations
    public void createMessage(int senderId, int receiverId, String content) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("sender_id", senderId);
        values.put("receiver_id", receiverId);
        values.put("content", content);
        values.put("is_read", 0);
        db.insert(DatabaseHelper.TABLE_MESSAGE, null, values);
    }

    @SuppressLint("Range")
    public List<com.example.rentingsystem.model.Message> getChatHistory(int userId1, int userId2) {
        List<com.example.rentingsystem.model.Message> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Get messages where (sender=u1 AND receiver=u2) OR (sender=u2 AND receiver=u1)
        String selection = "(sender_id=? AND receiver_id=?) OR (sender_id=? AND receiver_id=?)";
        String[] args = new String[]{
                String.valueOf(userId1), String.valueOf(userId2),
                String.valueOf(userId2), String.valueOf(userId1)
        };
        Cursor cursor = db.query(DatabaseHelper.TABLE_MESSAGE, null, selection, args, null, null, "create_time ASC");
        if (cursor != null) {
            while (cursor.moveToNext()) {
                com.example.rentingsystem.model.Message msg = new com.example.rentingsystem.model.Message();
                msg.setMsgId(cursor.getInt(cursor.getColumnIndex("msg_id")));
                msg.setSenderId(cursor.getInt(cursor.getColumnIndex("sender_id")));
                msg.setReceiverId(cursor.getInt(cursor.getColumnIndex("receiver_id")));
                msg.setContent(cursor.getString(cursor.getColumnIndex("content")));
                msg.setIsRead(cursor.getInt(cursor.getColumnIndex("is_read")));
                msg.setCreateTime(cursor.getString(cursor.getColumnIndex("create_time")));
                list.add(msg);
            }
            cursor.close();
        }
        return list;
    }

    // Get list of landlords the tenant has contacted (Distinct receiver_id where sender=tenant OR distinct sender_id where receiver=tenant)
    // Simplified: Just get distinct users from message table involving current user
    // And for each user, get the last message.
    // This is a bit complex in SQLite.
    // Strategy:
    // 1. Get all unique other_user_ids involved in chats with current_user_id.
    // 2. For each other_user_id, get the last message and user info.
    @SuppressLint("Range")
    public List<User> getChatUsers(int currentUserId) {
        List<User> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        
        // Find all unique users who exchanged messages with currentUserId
        String query = "SELECT DISTINCT other_id FROM ("
                + "SELECT receiver_id as other_id FROM " + DatabaseHelper.TABLE_MESSAGE + " WHERE sender_id = ? "
                + "UNION "
                + "SELECT sender_id as other_id FROM " + DatabaseHelper.TABLE_MESSAGE + " WHERE receiver_id = ?"
                + ")";
        
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(currentUserId), String.valueOf(currentUserId)});
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int otherId = cursor.getInt(0);
                User user = getUser(otherId);
                if (user != null) {
                    list.add(user);
                }
            }
            cursor.close();
        }
        return list;
    }

    public long addReservation(Reservation reservation) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("tenant_id", reservation.getTenantId());
        values.put("house_id", reservation.getHouseId());
        values.put("reserve_time", reservation.getReserveTime());
        values.put("status", 0); // Pending
        return db.insert(DatabaseHelper.TABLE_RESERVATION, null, values);
    }

    @SuppressLint("Range")
    public List<Reservation> getTenantReservations(int tenantId) {
        List<Reservation> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Join with House to get Title
        String query = "SELECT r.*, h.title as house_title FROM " + DatabaseHelper.TABLE_RESERVATION + " r "
                + "JOIN " + DatabaseHelper.TABLE_HOUSE + " h ON r.house_id = h.house_id "
                + "WHERE r.tenant_id = ? ORDER BY r.create_time DESC";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(tenantId)});
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Reservation res = cursorToReservation(cursor);
                res.setHouseTitle(cursor.getString(cursor.getColumnIndex("house_title")));
                list.add(res);
            }
            cursor.close();
        }
        return list;
    }

    @SuppressLint("Range")
    public List<Reservation> getLandlordReservations(int landlordId) {
        List<Reservation> list = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        // Join House and User(Tenant)
        String query = "SELECT r.*, h.title as house_title, u.real_name as tenant_name, u.phone as tenant_phone "
                + "FROM " + DatabaseHelper.TABLE_RESERVATION + " r "
                + "JOIN " + DatabaseHelper.TABLE_HOUSE + " h ON r.house_id = h.house_id "
                + "JOIN " + DatabaseHelper.TABLE_USER + " u ON r.tenant_id = u.user_id "
                + "WHERE h.landlord_id = ? ORDER BY r.create_time DESC";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(landlordId)});
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Reservation res = cursorToReservation(cursor);
                res.setHouseTitle(cursor.getString(cursor.getColumnIndex("house_title")));
                res.setTenantName(cursor.getString(cursor.getColumnIndex("tenant_name")));
                res.setTenantPhone(cursor.getString(cursor.getColumnIndex("tenant_phone")));
                list.add(res);
            }
            cursor.close();
        }
        return list;
    }

    public int updateReservationStatus(int reserveId, int status) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("status", status);
        return db.update(DatabaseHelper.TABLE_RESERVATION, values, "reserve_id=?", new String[]{String.valueOf(reserveId)});
    }

    @SuppressLint("Range")
    private Reservation cursorToReservation(Cursor cursor) {
        Reservation res = new Reservation();
        res.setReservationId(cursor.getInt(cursor.getColumnIndex("reserve_id")));
        res.setTenantId(cursor.getInt(cursor.getColumnIndex("tenant_id")));
        res.setHouseId(cursor.getInt(cursor.getColumnIndex("house_id")));
        res.setReserveTime(cursor.getString(cursor.getColumnIndex("reserve_time")));
        res.setStatus(cursor.getInt(cursor.getColumnIndex("status")));
        res.setCreateTime(cursor.getString(cursor.getColumnIndex("create_time")));
        return res;
    }
}
