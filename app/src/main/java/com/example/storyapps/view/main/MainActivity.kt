package com.example.storyapps.view.main

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.storyapps.WelcomeActivity
import com.example.storyapps.databinding.ActivityMainBinding
import com.example.storyapps.remote.response.ListStoryItem
import com.example.storyapps.view.ViewModelFactory
import com.example.storyapps.view.adapter.StoryAdapter
import com.example.storyapps.view.detail.DetailActivity
import com.example.storyapps.view.maps.MapsActivity
import com.example.storyapps.view.story.AddStoryActivity
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModels<MainViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private val storyAdapter: StoryAdapter by lazy { StoryAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getSession().observe(this) { isLogin ->
            if (!isLogin) {
                startActivity(Intent(this, WelcomeActivity::class.java))
                finish()
            }
        }
        viewModel.isLoading.observe(this) { isLoading ->
            showLoading(isLoading)
        }

        binding.btnAddStory.setOnClickListener() {
            val intent = Intent(this, AddStoryActivity::class.java)
            startActivity(intent)
        }

        binding.rvItem.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = storyAdapter
        }

        binding.ivMap.setOnClickListener() {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        // launch coroutine scope
        lifecycleScope.launch {
            viewModel.stories().observe(this@MainActivity) { result ->
                Log.d("DATA", "data: $result")
                storyAdapter.submitData(lifecycle, result)
                storyAdapter.setOnItemClickCallback(object : StoryAdapter.OnItemClickCallback {
                    override fun onItemClicked(
                        accountClicked: ListStoryItem?,
                        optionsCompat: ActivityOptionsCompat
                    ) {
                        val i = Intent(this@MainActivity, DetailActivity::class.java)
                        i.putExtra(DetailActivity.EXTRA_DESC, accountClicked?.description)
                        i.putExtra(DetailActivity.EXTRA_NAME, accountClicked?.name)
                        i.putExtra(DetailActivity.EXTRA_PHOTO, accountClicked?.photoUrl)
                        startActivity(i, optionsCompat.toBundle())
                    }
                })
            }
        }
        setupView()
        setupAction()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() {
        binding.ivLogout.setOnClickListener {
            viewModel.logout()
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

}