package ITI.covid19_tracker.view.Tab

import ITI.covid19_tracker.R
import ITI.covid19_tracker.view.settings.settingsMain2Activity
import ITI.covid19_tracker.view.statistics.statisticsFragment
import ITI.covid19_tracker.view.MainActivity
import ITI.covid19_tracker.view.instractions.Instractions
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.tab_activity.*


class ActivityTab : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tab_activity)

        setSupportActionBar(toolbar)
        val adapter = ViewPagerAdapter(supportFragmentManager)

        adapter.addFragment(MainActivity(), "Countries")
        adapter.addFragment(statisticsFragment(), "World Stat")
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(item.itemId == R.id.action_settings){
            startActivity(Intent(this, settingsMain2Activity::class.java))
        }else if(item.itemId == R.id.action_instructions) {
            startActivity(Intent(this, Instractions::class.java))
        }
            return super.onOptionsItemSelected(item)
    }
}
