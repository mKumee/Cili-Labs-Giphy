import com.chillilabs.feature.fake.FakeGiphyRepository
import com.chillilabs.feature.GiphyViewModel
import com.chillilabs.feature.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class GiphyViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: GiphyViewModel
    private val repository = FakeGiphyRepository()

    @Before
    fun setup() {
        viewModel = GiphyViewModel(repository)
    }

    @Test
    fun `query updates correctly`() = runTest {
        viewModel.onQueryChanged("cat")

        assertEquals("cat", viewModel.queryFlow.value)
    }

    @Test
    fun `loading state can be set`() = runTest {
        viewModel.setLoading(true)

        assertEquals(true, viewModel.uiState.value.loading)
    }

    @Test
    fun `error state can be set`() = runTest {
        viewModel.setErrorForTest("No internet")
        assertEquals("No internet", viewModel.uiState.value.error)
    }
}