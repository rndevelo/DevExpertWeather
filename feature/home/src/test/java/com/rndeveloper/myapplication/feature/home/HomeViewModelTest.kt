package com.rndeveloper.myapplication.feature.home

import com.rndeveloper.myapplication.domain.weather.usecases.GetWeatherUseCase
import com.rndeveloper.myapplication.testrules.CoroutinesTestRule
import org.junit.Before
import org.junit.Rule
import org.mockito.Mock

class HomeViewModelTest {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @Mock
    lateinit var getWeatherUseCase: GetWeatherUseCase

    private lateinit var vm: HomeViewModel

    @Before
    fun setUp(){
        vm = HomeViewModel(getWeatherUseCase)
    }
}