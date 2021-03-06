package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;


public class DBHelper extends SQLiteOpenHelper{

    public  static final String DATABASE_NAME = "180119X.db";

    public DBHelper(Context context){
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase sql) {

        sql.execSQL(
                "CREATE TABLE IF NOT EXISTS accounts(" +
                        "account_no TEXT PRIMARY KEY," +
                        "bank_name TEXT NOT NULL," +
                        "owner_name TEXT NOT NULL,"+
                        "initial_balance REAL NOT NULL" +
                        ");"
        );

        sql.execSQL(
                "CREATE TABLE IF NOT EXISTS transactions(" +
                        "transaction_id INTEGER PRIMARY KEY, " +
                        "date TEXT NOT NULL," +
                        "account_no TEXT NOT NULL," +
                        "type TEXT NOT NULL CHECK (type == \"INCOME\" OR type == \"EXPENSE\")," +
                        "amount REAL NOT NULL," +
                        "FOREIGN KEY(account_no) REFERENCES accounts(accounts_no) ON DELETE CASCADE " +
                        ");"
        );

    }

    @Override
    public void onUpgrade(SQLiteDatabase sql, int oldVersion, int newVersion) {
        sql.execSQL("DROP TABLE IF EXISTS accounts");
        sql.execSQL("DROP TABLE IF EXISTS transactions");
        onCreate(sql);
    }

    public boolean addNewAccount(String acc_no, String bank_name, String holder , double inital_balance){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues  contentValues = new ContentValues();
        contentValues.put("account_no",acc_no);
        contentValues.put("bank_name",bank_name);
        contentValues.put("owner_name",holder);
        contentValues.put("initial_balance",inital_balance);
        db.insert("accounts",null,contentValues);
        return true;
    }

    public boolean deleteAccount(String acc_no){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues  contentValues = new ContentValues();
        contentValues.put("account_no",acc_no);
        db.delete("accounts","account_no=?",new String[] {acc_no});
        return true;
    }

    public Cursor getAccountByNo(String acc_no){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM accounts WHERE account_no='"+acc_no+"';",null);
        return result;
    }

    public Cursor getAllAccountNo(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("SELECT account_no FROM accounts;",null);
        return result;
    }

    public Cursor getAllAccounts(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM accounts;",null);
        return result;
    }

    public boolean updateAccountBalance(double amount,String acc_no){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("account_no",acc_no);
        db.update("accounts", contentValues,"account_no=?",new String [] {acc_no});
        return true;
    }

    public Cursor getAllTransactions(){
        SQLiteDatabase db  = this.getReadableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM transactions;",null);
        return result;
    }

    public boolean addTransactionLog(String acc_no,String type,double amount,String date){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("account_no",acc_no);
        contentValues.put("type",type);
        contentValues.put("amount",amount);
        contentValues.put("date",date);
        db.insert("transactions",null, contentValues);
        return true;
    }

}
