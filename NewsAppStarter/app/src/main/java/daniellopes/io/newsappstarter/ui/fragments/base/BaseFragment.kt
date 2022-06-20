package daniellopes.io.newsappstarter.ui.fragments.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import daniellopes.io.newsappstarter.repository.NewsRepository

abstract class BaseFragment<VM : ViewModel, VB : ViewBinding> : Fragment() {

    //Instanciar um viewModel corretamente no fragmento

    protected lateinit var binding: VB
    protected lateinit var viewModel: VM

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = getFragmentBinding(inflater, container)
        val factory = ViewModelFactory(getFragmentRepository(), application = requireActivity().application)
        // o this é o Fragment, siginifica que quando a view for destruida, ele vai guardar uma referencia do viewModel, para quando ela for recriada,
        // ela receber o mesmo viewModel, posso usar tambem o viewModelStore
        viewModel = ViewModelProvider(viewModelStore, factory).get(getViewModel())

        return binding.root
    }

    abstract fun getViewModel(): Class<VM>
    abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): VB
    abstract fun getFragmentRepository(): NewsRepository
}