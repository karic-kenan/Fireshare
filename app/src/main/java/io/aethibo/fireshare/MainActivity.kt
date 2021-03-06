package io.aethibo.fireshare

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pandora.bottomnavigator.BottomNavigator
import io.aethibo.fireshare.ui.createpost.view.CreatePostFragment
import io.aethibo.fireshare.ui.discovery.view.DiscoveryFragment
import io.aethibo.fireshare.ui.notificationsfeed.NotificationsFeedFragment
import io.aethibo.fireshare.ui.profile.view.ProfileFragment
import io.aethibo.fireshare.ui.timeline.TimelineFragment

class MainActivity : AppCompatActivity() {

    private lateinit var navigator: BottomNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigator = BottomNavigator.onCreate(
                fragmentContainer = R.id.nav_host_container,
                bottomNavigationView = findViewById(R.id.nav_view),
                rootFragmentsFactory = mapOf(
                        R.id.timeline to { TimelineFragment.newInstance() },
                        R.id.discovery to { DiscoveryFragment.newInstance() },
                        R.id.add to { CreatePostFragment.newInstance() },
                        R.id.feed to { NotificationsFeedFragment.newInstance() },
                        R.id.profile to { ProfileFragment.newInstance() }
                ),
                defaultTab = R.id.timeline,
                activity = this
        )
    }

    override fun onBackPressed() {
        if (!navigator.pop())
            super.onBackPressed()
    }
}