package ITI.covid19_tracker.view

import ITI.covid19_tracker.R
import ITI.covid19_tracker.model.Country
import ITI.covid19_tracker.viewmodel.MainViewModel
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.recyclerview.widget.RecyclerView

class MainAdapter(private val context: Context, private val country: List<Country>?, ViewModel: MainViewModel?) :
    RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    var viewmodel: MainViewModel? = ViewModel

    override fun onCreateViewHolder(viewGroup: ViewGroup, index: Int): ViewHolder {
        val rootView =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.card_main, viewGroup, false)

      //  viewmodel = ViewModel
        return ViewHolder(rootView)
    }

    override fun getItemCount(): Int {
        return country?.size!!
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, index: Int) {

        viewHolder.itemView.animation =
            AnimationUtils.loadAnimation(context,
                R.anim.item_animation_fall_down
            )

        viewHolder.country_name.text = country?.get(index)?.country_name
//        println("country_name" + country?.get(index)?.country_name)
        viewHolder.cases.text = country?.get(index)?.cases
  //      println("cases" + country?.get(index)?.cases)
        viewHolder.new_cases.text = country?.get(index)?.new_cases
    //    println("new_cases" + country?.get(index)?.new_cases)
        viewHolder.deaths.text = country?.get(index)?.deaths
      //  println("deaths" + country?.get(index)?.deaths)
        viewHolder.new_deaths.text = country?.get(index)?.new_deaths
        //println("new_deaths" + country?.get(index)?.new_deaths)
        viewHolder.total_recovered.text = country?.get(index)?.total_recovered
        viewHolder.total_cases_per_1m_population.text =
            country?.get(index)?.Subscribe

        viewHolder.showdetails.setOnClickListener {
            if (viewHolder.Details.visibility == View.VISIBLE) {
                viewHolder.Details.visibility = View.GONE
                viewHolder.showdetails.text = "Show Details"
            } else {
                viewHolder.Details.visibility = View.VISIBLE
                viewHolder.showdetails.text = "Hide Details"

            }
        }

        viewHolder.subscribe_id.setOnClickListener {
            if(country?.get(index)?.Subscribe.equals("0"))
            {
                viewHolder.subscribe_id.text = "UNSUBSCRIBE"
                viewmodel?.updateSubCountry(country?.get(index)?.country_name, "1")

            }else{
                viewHolder.subscribe_id.text = "SUBSCRIBE"
                viewmodel?.updateSubCountry(country?.get(index)?.country_name, "0")
            }
        }

        if(country?.get(index)?.Subscribe.equals("1"))
        {
            viewHolder.subscribe_id.text = "UNSUBSCRIBE"
        }

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var country_name: TextView = itemView.findViewById(R.id.country_name_id) as TextView
        var cases: TextView = itemView.findViewById(R.id.cases_id) as TextView
        var new_cases: TextView = itemView.findViewById(R.id.new_cases_id) as TextView
        var deaths: TextView = itemView.findViewById(R.id.death_id) as TextView
        var new_deaths: TextView = itemView.findViewById(R.id.new_death_id) as TextView
        var total_recovered: TextView = itemView.findViewById(R.id.total_recovered_id) as TextView
        var showdetails: Button = itemView.findViewById(R.id.details_id) as Button
        var Details: LinearLayoutCompat =
            itemView.findViewById(R.id.CountryDetails_id) as LinearLayoutCompat
        var total_cases_per_1m_population: TextView =
            itemView.findViewById(R.id.total_cases_per_1m_population) as TextView
        var subscribe_id:Button =  itemView.findViewById(R.id.subscribe_id) as Button
    }

}