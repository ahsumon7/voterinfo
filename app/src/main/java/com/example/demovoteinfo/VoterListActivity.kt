package com.example.demovoteinfo

import com.example.demovoteinfo.model.AreaGroup
import com.example.demovoteinfo.model.VotingCenter
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.demovoteinfo.R
import com.example.demovoteinfo.adapter.ExpandableListAdapter

// Define your item types for the adapter
private const val ITEM_TYPE_AREA_GROUP = 0
private const val ITEM_TYPE_VOTING_CENTER = 1

// A data class to hold flattened items for the RecyclerView
sealed class DisplayableItem {
    data class AreaGroupItem(val areaGroup: AreaGroup) : DisplayableItem()
    data class VotingCenterItem(val votingCenter: VotingCenter, val areaGroupName: String) :
        DisplayableItem()
}

class ExpandableListAdapter(
    private var areaGroups: List<AreaGroup>,
    private val onVotingCenterClick: (VotingCenter) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items: MutableList<DisplayableItem> = mutableListOf()
    private val expandedStates: MutableMap<String, Boolean> =
        mutableMapOf() // To store expanded state of groups

    init {
        updateItems()
    }

    private fun updateItems() {
        items.clear()
        areaGroups.forEach { group ->
            items.add(DisplayableItem.AreaGroupItem(group))
            if (expandedStates[group.name] == true) { // Only add centers if group is expanded
                group.votingCenters.forEach { center ->
                    items.add(DisplayableItem.VotingCenterItem(center, group.name))
                }
            }
        }
        notifyDataSetChanged() // Or use more specific notify methods
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is DisplayableItem.AreaGroupItem -> ITEM_TYPE_AREA_GROUP
            is DisplayableItem.VotingCenterItem -> ITEM_TYPE_VOTING_CENTER
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ITEM_TYPE_AREA_GROUP -> {
                // Inflate your layout for AreaGroup
                // Example: val view = inflater.inflate(R.layout.item_area_group, parent, false)
                // return AreaGroupViewHolder(view)
                // Placeholder:
                val view = TextView(parent.context)
                AreaGroupViewHolder(view)

            }

            ITEM_TYPE_VOTING_CENTER -> {
                // Inflate your layout for VotingCenter
                // Example: val view = inflater.inflate(R.layout.item_voting_center, parent, false)
                // return VotingCenterViewHolder(view, onVotingCenterClick)
                // Placeholder:
                val view = TextView(parent.context)
                VotingCenterViewHolder(view, onVotingCenterClick)
            }

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }



    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = items[position]) {
            is DisplayableItem.AreaGroupItem -> {
                (holder as AreaGroupViewHolder).bind(item.areaGroup)
                holder.itemView.setOnClickListener {
                    // Toggle expansion state
                    val groupName = item.areaGroup.name
                    expandedStates[groupName] = !(expandedStates[groupName] ?: false)
                    updateItems() // Rebuild the list based on new expanded states
                }
            }

            is DisplayableItem.VotingCenterItem -> {
                (holder as VotingCenterViewHolder).bind(item.votingCenter)
            }
        }
    }

    override fun getItemCount(): Int = items.size

    // --- ViewHolder Classes ---

/*    class AreaGroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Example: val groupNameTextView: TextView = itemView.findViewById(R.id.groupName)
        fun bind(areaGroup: AreaGroup) {
            // Bind areaGroup data to your views
            (itemView as? TextView)?.text = areaGroup.name // Placeholder
        }
    }*/

    class AreaGroupViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvGroupName: TextView = itemView.findViewById(R.id.tvGroupName)
        private val tvGroupSubTitle: TextView = itemView.findViewById(R.id.tvGroupSubTitle)

        fun bind(areaGroup: AreaGroup) {
            tvGroupName.text = areaGroup.name
            tvGroupSubTitle.text = "মোট কেন্দ্র: ${areaGroup.votingCenters.size}" // Optional
        }
    }


    class VotingCenterViewHolder(
        itemView: View,
        private val onVotingCenterClick: (VotingCenter) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        // Example: val centerNameTextView: TextView = itemView.findViewById(R.id.centerName)
        // val maleVotersTextView: TextView = itemView.findViewById(R.id.maleVoters)
        // val femaleVotersTextView: TextView = itemView.findViewById(R.id.femaleVoters)

        fun bind(votingCenter: VotingCenter) {
            // Bind votingCenter data to your views
            // Example:
            // centerNameTextView.text = votingCenter.name
            // maleVotersTextView.text = "Male: ${votingCenter.maleVoters}"
            // femaleVotersTextView.text = "Female: ${votingCenter.femaleVoters}"
            (itemView as? TextView)?.text = "   ${votingCenter.name}" // Placeholder

            itemView.setOnClickListener {
                onVotingCenterClick(votingCenter)
            }
        }
    }

    // Optional: Method to update data if it changes
    fun updateData(newAreaGroups: List<AreaGroup>) {
        areaGroups = newAreaGroups
        // Reset expanded states or maintain them based on your preference
        // For simplicity, let's reset them here or you might want to preserve them based on group names
        expandedStates.clear()
        updateItems()
    }
}


class VoterListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExpandableListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_voter_list)
        title = getString(R.string.title_activity_voter_list)

        recyclerView = findViewById(R.id.rvVoterList)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val sampleData = getSampleData()
        adapter = ExpandableListAdapter(sampleData) { votingCenter ->
            // Handle click on voting center
            val intent = Intent(this, VoterDetailActivity::class.java)
            intent.putExtra(VoterDetailActivity.EXTRA_VOTING_CENTER, votingCenter)
            startActivity(intent)
        }
        recyclerView.adapter = adapter
    }

    private fun getSampleData(): List<AreaGroup> {
        return listOf(
            AreaGroup(
                name = "শেরপুর পৌরসভা-১",
                votingCenters = listOf(
                    VotingCenter(name = "১। নবীনগর সরকারি প্রাথমিক বিদ্যালয়", maleVoters = 1200, femaleVoters = 1150, thirdGenderVoters = 2,contactName = "Mr. Karim", contactPhone = "+8801711122233"),
                    VotingCenter(name = "২। পৌরসভার মডেল স্কুল", maleVoters = 950, femaleVoters = 980, thirdGenderVoters = 1,contactName = "Mr. Karim", contactPhone = "+8801711122233"),
                    VotingCenter(name = "১। নবীনগর সরকারি প্রাথমিক বিদ্যালয়", maleVoters = 1200, femaleVoters = 1150, thirdGenderVoters = 2,contactName = "Mr. Karim", contactPhone = "+8801711122233"),
                    VotingCenter(name = "২। পৌরসভার মডেল স্কুল", maleVoters = 950, femaleVoters = 980, thirdGenderVoters = 1,contactName = "Mr. Karim", contactPhone = "+8801711122233"),
                    VotingCenter(name = "১। নবীনগর সরকারি প্রাথমিক বিদ্যালয়", maleVoters = 1200, femaleVoters = 1150, thirdGenderVoters = 2,contactName = "Mr. Karim", contactPhone = "+8801711122233"),
                    VotingCenter(name = "২। পৌরসভার মডেল স্কুল", maleVoters = 950, femaleVoters = 980, thirdGenderVoters = 1,contactName = "Mr. Karim", contactPhone = "+8801711122233"),
                    VotingCenter(name = "১। নবীনগর সরকারি প্রাথমিক বিদ্যালয়", maleVoters = 1200, femaleVoters = 1150, thirdGenderVoters = 2,contactName = "Mr. Karim", contactPhone = "+8801711122233"),
                    VotingCenter(name = "২। পৌরসভার মডেল স্কুল", maleVoters = 950, femaleVoters = 980, thirdGenderVoters = 1,contactName = "Mr. Karim", contactPhone = "+8801711122233"),
                    VotingCenter(name = "১। নবীনগর সরকারি প্রাথমিক বিদ্যালয়", maleVoters = 1200, femaleVoters = 1150, thirdGenderVoters = 2,contactName = "Mr. Karim", contactPhone = "+8801711122233"),
                    VotingCenter(name = "২। পৌরসভার মডেল স্কুল", maleVoters = 950, femaleVoters = 980, thirdGenderVoters = 1)
                )
            ),
            AreaGroup(
                name = "ইউনিয়ন-১৪টি",
                votingCenters = listOf(
                    VotingCenter(name = "১। কামারিয়া ইউনিয়ন পরিষদ", maleVoters = 2500, femaleVoters = 2400, thirdGenderVoters = 5),
                    VotingCenter(name = "২। ভাতশালা ইউনিয়ন কমপ্লেক্স", maleVoters = 1800, femaleVoters = 1750, thirdGenderVoters = 3),
                    VotingCenter(name = "৩। চরপক্ষীমারী উচ্চ বিদ্যালয়", maleVoters = 2200, femaleVoters = 2100, thirdGenderVoters = 4),
                    VotingCenter(name = "4। কামারিয়া ইউনিয়ন পরিষদ", maleVoters = 2500, femaleVoters = 2400, thirdGenderVoters = 5),
                    VotingCenter(name = "২2। ভাতশালা ইউনিয়ন কমপ্লেক্স", maleVoters = 1800, femaleVoters = 1750, thirdGenderVoters = 3),
                    VotingCenter(name = "৩3। চরপক্ষীমারী উচ্চ বিদ্যালয়", maleVoters = 2200, femaleVoters = 2100, thirdGenderVoters = 4)
                )
            )
            // Add more sample AreaGroups here
        )
    }
}
