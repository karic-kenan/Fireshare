package io.aethibo.fireshare

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.pandora.bottomnavigator.BottomNavigator
import io.aethibo.fireshare.features.addpost.view.AddPostFragment
import io.aethibo.fireshare.features.discovery.view.DiscoveryFragment
import io.aethibo.fireshare.features.feed.view.FeedFragment
import io.aethibo.fireshare.features.profile.view.ProfileFragment
import io.aethibo.fireshare.features.timeline.view.TimelineFragment

class MainActivity : AppCompatActivity() {

    private lateinit var navigator: BottomNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigator = BottomNavigator.onCreate(
            fragmentContainer = R.id.nav_host_container,
            bottomNavigationView = findViewById(R.id.nav_view),
            rootFragmentsFactory = mapOf(
                R.id.timeline to { TimelineFragment() },
                R.id.discovery to { DiscoveryFragment() },
                R.id.add to { AddPostFragment() },
                R.id.feed to { FeedFragment() },
                R.id.profile to { ProfileFragment() }
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