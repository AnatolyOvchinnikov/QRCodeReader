package com.app.qrcodeapplication.ui.history

import android.os.Bundle
import android.view.*
import androidx.navigation.fragment.findNavController
import com.app.qrcodeapplication.R
import com.app.qrcodeapplication.entity.Check
import com.app.qrcodeapplication.presentation.history.ScansHistoryPresenter
import com.app.qrcodeapplication.presentation.history.ScansHistoryView
import com.app.qrcodeapplication.ui.MainActivity
import com.app.qrcodeapplication.ui.global.BaseFragment
import com.app.qrcodeapplication.ui.lists.ScansHistoryAdapter
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.arellomobile.mvp.presenter.ProvidePresenter
import kotlinx.android.synthetic.main.fragment_scans_history_list.*


class ScansHistoryFragment : BaseFragment(), ScansHistoryView {

    @InjectPresenter(type = PresenterType.GLOBAL, tag = PRESENTER_TAG)
    lateinit var presenter: ScansHistoryPresenter

    @ProvidePresenter(type = PresenterType.GLOBAL, tag = PRESENTER_TAG)
    fun provideScansHistoryPresenter() = ScansHistoryPresenter()

    private val adapter: ScansHistoryAdapter = ScansHistoryAdapter(emptyList())

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_scans_history_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as MainActivity).supportActionBar?.setTitle(getString(R.string.scan_history))
        setHasOptionsMenu(true)
        list.adapter = adapter
        presenter.loadList()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear();
        inflater.inflate(R.menu.app_menu, menu);
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.scan) {
            loadScannerFragment()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    private fun loadScannerFragment() {
        findNavController().navigate(ScansHistoryFragmentDirections.actionScansHistoryFragmentToScanFragment())
    }

    override fun loadItems(checkList: List<Check>) {
        updateAdapter(checkList)
    }

    private fun updateAdapter(checkList: List<Check>) {
        val recyclerViewState = list.getLayoutManager()?.onSaveInstanceState()
        adapter.updateData(checkList)
        list?.getLayoutManager()?.onRestoreInstanceState(recyclerViewState)
    }

    override fun showProgress(progress: Boolean) {
        if(progress) {
            showProgressDialog()
        } else {
            hideProgressDialog()
        }
    }

    companion object {
        private const val PRESENTER_TAG = "ScansHistoryPresenter"
    }
}