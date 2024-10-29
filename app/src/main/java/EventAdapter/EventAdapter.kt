package za.varsitycollege.syncup_demo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import za.varsitycollege.syncup_demo.EventDomain
import za.varsitycollege.syncup_demo.R

class EventAdapter(private val eventList: ArrayList<EventDomain>) : RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.viewholder_event_list, parent, false)
        context = parent.context
        return ViewHolder(inflater)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = eventList[position]
        holder.title.text = event.name
        holder.location.text = event.location
        holder.time.text = event.time
        holder.price.text = event.price
        holder.genre.text = event.genre // Display genre

        // Here, you can load images if you have an image for each genre or event
        // Otherwise, you might not need Glide for this purpose
        // For now, it will try to find an image based on the genre name
        val drawableResourceId = holder.itemView.resources.getIdentifier(event.genre.toLowerCase(), "drawable", context.packageName)
        if (drawableResourceId != 0) {
            holder.pic.setImageResource(drawableResourceId)
        }
    }

    override fun getItemCount(): Int {
        return eventList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.event_name) // Adjusted ID from viewholder_event_list.xml
        val location: TextView = itemView.findViewById(R.id.event_location) // Adjusted ID
        val time: TextView = itemView.findViewById(R.id.event_time) // Adjusted ID
        val genre: TextView = itemView.findViewById(R.id.event_genre) // Adjusted ID
        val price: TextView = itemView.findViewById(R.id.event_price) // Adjusted ID
        val pic: ImageView = itemView.findViewById(R.id.event_image) // Assuming this ImageView exists
    }
}
