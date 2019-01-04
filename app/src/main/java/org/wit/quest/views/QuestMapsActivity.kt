package org.wit.quest.views

import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.wit.quest.R

import kotlinx.android.synthetic.main.activity_quest_maps.*
import kotlinx.android.synthetic.main.content_quest_maps.*
import org.jetbrains.anko.startActivityForResult
import org.wit.placemark.helpers.readImageFromPath
import org.wit.quest.main.MainApp

class QuestMapsActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener, GoogleMap.OnMarkerClickListener {

  lateinit var map: GoogleMap
  lateinit var app: MainApp

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_quest_maps)
    setSupportActionBar(toolbarMap)

    app = application as MainApp

    mapView.onCreate(savedInstanceState);

    mapView.getMapAsync {
      map = it
      configureMap()
    }

    // add navigation drawer
    val toggle = ActionBarDrawerToggle(this, drawer_layout, toolbarMap, R.string.navigation_drawer_open, R.string.navigation_drawer_close)
    drawer_layout.addDrawerListener(toggle)
    toggle.syncState()

    nav_view.setNavigationItemSelectedListener(this)
  }

  override fun onDestroy() {
    super.onDestroy()
    mapView.onDestroy()
  }

  override fun onLowMemory() {
    super.onLowMemory()
    mapView.onLowMemory()
  }

  override fun onPause() {
    super.onPause()
    mapView.onPause()
  }

  override fun onResume() {
    super.onResume()
    mapView.onResume()
  }

  override fun onSaveInstanceState(outState: Bundle?) {
    super.onSaveInstanceState(outState)
    mapView.onSaveInstanceState(outState)
  }

  override fun onMarkerClick(marker: Marker): Boolean {
    currentName.text = marker.title
    app.users.findAllQuests().forEach() {
      if (it.id == marker.tag) {
        currentTownland.text = it.townland

        map_questImage.visibility = View.GONE
        map_questImageDefault.visibility = View.VISIBLE

        if (it.images.size != 0) {
          map_questImage.setImageBitmap(readImageFromPath(this, it.images.get(0)))
          map_questImage.visibility = View.VISIBLE
          map_questImageDefault.visibility = View.GONE
        }
      }
    }
    return false
  }

  fun configureMap() {
    map.uiSettings.setZoomControlsEnabled(true)
    map.setOnMarkerClickListener(this)
    app.users.findAllQuests().forEach {
      val loc = LatLng(it.lat, it.lng)
      val options = MarkerOptions().title(it.name).position(loc)
      map.addMarker(options).tag = it.id
      map.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, it.zoom))
    }
  }

  override fun onBackPressed() {
    // close drawer on back pressed
    if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
      drawer_layout.closeDrawer(GravityCompat.START)
    } else {
      super.onBackPressed()
    }
  }

  override fun onNavigationItemSelected(item: MenuItem): Boolean {
    // handle menu item selection
    when (item.itemId) {
      R.id.item_home -> startActivityForResult<HomeActivity>(200)
      R.id.item_list -> startActivityForResult<ListView>(200)
      R.id.item_add -> startActivityForResult<QuestView>(200)
      R.id.item_setting -> startActivityForResult<SettingsActivity>(200)
    }

    drawer_layout.closeDrawer(GravityCompat.START)
    return true
  }
}
