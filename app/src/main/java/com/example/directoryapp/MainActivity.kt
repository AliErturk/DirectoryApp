package com.example.directoryapp

import android.database.sqlite.SQLiteException
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.Menu
import android.widget.EditText

import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView

import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.directoryapp.databinding.ActivityMainBinding
import com.info.sqlitekullanimihazirveritabani.DatabaseCopyHelper

class MainActivity : AppCompatActivity(),SearchView.OnQueryTextListener {
    private lateinit var design : ActivityMainBinding
    private lateinit var personsAdapter: PersonsAdapter
    private lateinit var dbHelper: DBHelper
    private lateinit var personList: ArrayList<Persons>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        design=DataBindingUtil.setContentView(this,R.layout.activity_main)
        copyDatabase()
        design.toolbar.title= "Directory"
        setSupportActionBar(design.toolbar)

        design.recyclerView.layoutManager= LinearLayoutManager(this)
        design.recyclerView.setHasFixedSize(true)
        dbHelper= DBHelper(this)
        showPersons()

        design.floatingActionButtonAdd.setOnClickListener {


            showAlert()
        }


    }
    fun copyDatabase(){
        val dbc =DatabaseCopyHelper(this)

        try {
            dbc.createDataBase()
        }catch (e: SQLiteException){
            Error("Unable to create database")
        }

        try {
            dbc.openDataBase()
        }catch (e: SQLiteException){
            Error("Unable to open database")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_menu,menu)
        val item= menu?.findItem(R.id.action_search)
        val searchView= item?.actionView as SearchView

        searchView.setOnQueryTextListener(this)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        search(query)
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        search(newText)
        return true
    }
    fun search(query: String?){
        personList= Personsdao().searchPerson(dbHelper,query.toString())
        personsAdapter= PersonsAdapter(this,personList,dbHelper)
        design.recyclerView.adapter=personsAdapter

    }
    fun showPersons(){
        personList= Personsdao().getAllNumbers(dbHelper)
        personsAdapter= PersonsAdapter(this,personList,dbHelper)
        design.recyclerView.adapter=personsAdapter

    }
    fun showAlert(){
        val design = LayoutInflater.from(this).inflate(R.layout.alert_design,null)
        val editTextPersonName = design.findViewById(R.id.editTextTextPersonName) as EditText
        val editTextPersonPhone = design.findViewById(R.id.editTextTextPersonPhone) as EditText

        val ad = AlertDialog.Builder(this)

        ad.setTitle("Add Person")
        ad.setView(design)

        ad.setPositiveButton("Add"){ dialogInterface,i ->
            val personName= editTextPersonName.text.toString().trim()
            val personPhone=editTextPersonPhone.text.toString().trim()
            inputTextController(personName,personPhone)
            val controller= inputTextController(personName,personPhone)
            if (controller){
                Personsdao().addPerson(dbHelper,personName,personPhone)
                showPersons()
            }else{
                showAlert()
            }
        }
        ad.setNegativeButton("Cancel"){ dialogInterface,i ->

        }

        ad.create().show()
    }
    fun inputTextController(personName: String,personPhone:String) : Boolean{
        if (TextUtils.isEmpty(personName)){
            Toast.makeText(this,"Name can not be empty.",Toast.LENGTH_SHORT).show()
            return false
        }
        if (TextUtils.isEmpty(personPhone)){
            Toast.makeText(this,"Phone can not be empty.",Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

}