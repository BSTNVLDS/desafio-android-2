package com.accenture.base.extensions

import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.NavHostFragment

fun FragmentActivity.changeStart(idNavHost: Int, idNavGraph: Int, id: Int) {
    val navHostFragment =
        supportFragmentManager.findFragmentById(idNavHost) as NavHostFragment
    val inflater = navHostFragment.navController.navInflater
    val graph = inflater.inflate(idNavGraph)
    graph.setStartDestination(id)
    val navController = navHostFragment.navController
    navController.setGraph(graph, intent.extras)
}