package com.app.computacionysociedad.systemcontrol;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Fragment fragment = null;
    RelativeLayout relativeLayout;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LoginActivity.loginActivity.finish();

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        relativeLayout = findViewById(R.id.fragment_status);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);
        TextView name = header.findViewById(R.id.userTw);
        TextView email = header.findViewById(R.id.emailTw);
        ImageView imageProfile = header.findViewById(R.id.imageProfile);

        String user = getIntent().getStringExtra("name");
        String emailText = getIntent().getStringExtra("email");
        String imageUri = getIntent().getStringExtra("imageUri");

        Picasso.with(this).load(imageUri).placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher)
                .into(imageProfile);
        name.setText(user);
        email.setText(emailText);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displaySelectedScreen(int id){

        switch (id){
            case R.id.report:
                toolbar.setTitle(R.string.reportar);
                fragment = new ReportFragment();
                relativeLayout.setVisibility(View.INVISIBLE);
                break;
            case R.id.route:
                toolbar.setTitle(R.string.ConsultarR);
                fragment = new QueryRouteFragment();
                relativeLayout.setVisibility(View.INVISIBLE);
                break;
            case R.id.zone:
                toolbar.setTitle(R.string.ConsultarZ);
                fragment = new QueryZoneFragment();
                relativeLayout.setVisibility(View.INVISIBLE);
                break;
            case R.id.acerca:
                toolbar.setTitle(R.string.sobre);
                fragment = new AboutFragmentClass();
                relativeLayout.setVisibility(View.INVISIBLE);
        }

        if(fragment != null){
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_main, fragment);
            ft.commit();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        displaySelectedScreen(id);
        return true;
    }

}
