package com.example.directoryapp

import android.content.ContentValues

class Personsdao {

    fun getAllNumbers(dbHelper: DBHelper) : ArrayList<Persons>{
        val personList = ArrayList<Persons>()
        val db= dbHelper.writableDatabase
        val cursor = db.rawQuery("SELECT*FROM kisiler",null)

        while (cursor.moveToNext()){
            val person= Persons(
                cursor.getInt(cursor.getColumnIndexOrThrow("kisi_id")),
                cursor.getString(cursor.getColumnIndexOrThrow("kisi_ad")),
                cursor.getString(cursor.getColumnIndexOrThrow("kisi_tel"))
            )
            personList.add(person)
        }
        return personList
    }

    fun deleteNumber(dbHelper: DBHelper,kisi_id:Int){
        val db= dbHelper.writableDatabase
        db.delete("kisiler","kisi_id=?", arrayOf(kisi_id.toString()))
        db.close()
    }
    fun addPerson(dbHelper: DBHelper,kisi_ad:String,kisi_tel:String){

        val db= dbHelper.writableDatabase

        val values= ContentValues()

        values.put("kisi_ad",kisi_ad)
        values.put("kisi_tel",kisi_tel)

        db.insertOrThrow("kisiler",null,values)

        db.close()
    }

    fun updateNumber(dbHelper: DBHelper,kisi_id: Int,kisi_ad:String,kisi_tel:String){

        val db= dbHelper.writableDatabase

        val values= ContentValues()

        values.put("kisi_ad",kisi_ad)
        values.put("kisi_tel",kisi_tel)

        db.update("kisiler",values,"kisi_id=?", arrayOf(kisi_id.toString()))

        db.close()
    }

    fun searchPerson(dbHelper: DBHelper,searchingText:String) : ArrayList<Persons>{
        val personList = ArrayList<Persons>()
        val db= dbHelper.writableDatabase
        val cursor = db.rawQuery("SELECT*FROM kisiler WHERE kisi_ad like '$searchingText%'",null)

        while (cursor.moveToNext()){
            val person= Persons(
                cursor.getInt(cursor.getColumnIndexOrThrow("kisi_id")),
                cursor.getString(cursor.getColumnIndexOrThrow("kisi_ad")),
                cursor.getString(cursor.getColumnIndexOrThrow("kisi_tel"))
            )
            personList.add(person)
        }
        return personList
    }


}