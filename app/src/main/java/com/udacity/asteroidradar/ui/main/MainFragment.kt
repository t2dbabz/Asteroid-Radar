package com.udacity.asteroidradar.ui.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.adapter.AsteroidAdapter
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {



    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity){}

        ViewModelProvider(this, MainViewModelFactory(activity.application)).get(MainViewModel::class.java)
    }

    private lateinit var binding: FragmentMainBinding

    private lateinit var adapter: AsteroidAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = AsteroidAdapter(AsteroidAdapter.OnClickListener{
            findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
        })

        binding.asteroidRecycler.adapter = adapter
        viewModel.asteroids.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_week_asteroids -> {
                viewModel.getWeekAsteroid().observe(viewLifecycleOwner, {
                    adapter.submitList(it)
                })
            }
            R.id.show_today_asteroids -> {
                viewModel.getTodayAsteroids().observe(viewLifecycleOwner, {
                    adapter.submitList(it)
                })
            }
            R.id.show_all_asteroids -> {
                viewModel.asteroids.observe(viewLifecycleOwner, {
                    adapter.submitList(it)
                })
            }
        }
        return true
    }
}
