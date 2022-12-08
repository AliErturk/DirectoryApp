package com.example.directoryapp

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class PersonsAdapter(private val context: Context,private var personList:List<Persons>,private val dbHelper: DBHelper)
                    :RecyclerView.Adapter<PersonsAdapter.CardViewHolder>(){


    inner class CardViewHolder(view: View) : RecyclerView.ViewHolder(view){
        var textViewNamePhone: TextView
        var imageViewMenu : ImageView
        var cardViewPersons :CardView

        init {
            textViewNamePhone = view.findViewById(R.id.textViewNamePhone)
            imageViewMenu= view.findViewById(R.id.imageViewMenu)
            cardViewPersons= view.findViewById(R.id.cardViewPersons)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val design= LayoutInflater.from(context).inflate(R.layout.card_view_persons,parent,false)
        return CardViewHolder(design)

    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val person= personList[position]
        holder.textViewNamePhone.text="${person.kisi_ad} - ${person.kisi_tel}"


        holder.imageViewMenu.setOnClickListener { item->

            val popup=PopupMenu(context,holder.imageViewMenu)
            popup.menuInflater.inflate(R.menu.card_menu,popup.menu)

            popup.setOnMenuItemClickListener { item->
                when(item.itemId){
                    R.id.action_delete ->{
                        Snackbar.make(holder.imageViewMenu,"Do you want to delete ${person.kisi_ad} ?",Snackbar.LENGTH_LONG)
                            .setAction("Yes"){
                                Personsdao().deleteNumber(dbHelper,person.kisi_id)
                                personList=Personsdao().getAllNumbers(dbHelper)
                                notifyDataSetChanged()

                            }.show()

                        true
                    }
                    R.id.action_update ->{
                        showAlert(person)

                        true
                    }
                    else-> false
                }
            }
            popup.show()

        }


    }
    fun showAlert(person: Persons){
        val design = LayoutInflater.from(context).inflate(R.layout.alert_design,null)
        val editTextPersonName = design.findViewById(R.id.editTextTextPersonName) as EditText
        val editTextPersonPhone = design.findViewById(R.id.editTextTextPersonPhone) as EditText

        editTextPersonName.setText(person.kisi_ad)
        editTextPersonPhone.setText(person.kisi_tel)

        val ad = AlertDialog.Builder(context)

        ad.setTitle("Update Person")
        ad.setView(design)

        ad.setPositiveButton("Update"){ dialogInterface,i ->
            val personName= editTextPersonName.text.toString().trim()
            val personPhone=editTextPersonPhone.text.toString().trim()
            inputTextController(personName,personPhone)
            val controller= inputTextController(personName,personPhone)
            if (controller){
                Personsdao().updateNumber(dbHelper,person.kisi_id,personName,personPhone)
                personList=Personsdao().getAllNumbers(dbHelper)
                notifyDataSetChanged()

            }else{
                showAlert(person)
            }


        }
        ad.setNegativeButton("Cancel"){ dialogInterface,i ->

        }

        ad.create().show()
    }
    fun inputTextController(personName: String,personPhone:String) : Boolean{
        if (TextUtils.isEmpty(personName)){
            Toast.makeText(context,"Name can not be empty.",Toast.LENGTH_SHORT).show()
            return false
        }
        if (TextUtils.isEmpty(personPhone)){
            Toast.makeText(context,"Phone can not be empty.",Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    override fun getItemCount(): Int {
        return personList.size
    }

}