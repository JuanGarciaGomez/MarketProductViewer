package com.juanfe.project.marketproductviewer.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class DataStoreManagerTest {

    @Mock
    private lateinit var dataStore: DataStore<Preferences>

    private lateinit var dataStoreManager: DataStoreManager

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        dataStoreManager = DataStoreManager(dataStore)
        assertNotNull(DataStoreManager.SEARCH_HISTORY_KEY)
    }

    @Test
    fun `test saveSearch stores the query`() = runTest {
        val query = "example"
        runBlocking {
            dataStoreManager.saveSearch(query)
        }
    }

}
