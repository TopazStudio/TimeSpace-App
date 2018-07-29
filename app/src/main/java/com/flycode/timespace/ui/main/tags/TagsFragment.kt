package com.flycode.timespace.ui.main.tags


import android.databinding.DataBindingUtil
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.afollestad.materialdialogs.MaterialDialog
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.beloo.widget.chipslayoutmanager.SpacingItemDecoration
import com.flycode.timespace.R
import com.flycode.timespace.data.models.Tag
import com.flycode.timespace.databinding.CustomTagEntryBinging
import com.flycode.timespace.databinding.TagsBinding
import com.flycode.timespace.ui.base.BaseFragment
import com.flycode.timespace.ui.main.MainActivity
import com.thebluealliance.spectrum.SpectrumDialog
import kotlinx.android.synthetic.main.fragment_tags.*
import javax.inject.Inject

class TagsFragment : BaseFragment<TagsFragment, TagsPresenter, TagsViewModel>(),
        TagsContract.TagsFragment {

    @Inject override lateinit var viewModel: TagsViewModel
    @Inject lateinit var mainActivity: MainActivity
    @Inject lateinit var tagsAdapter: TagsAdapter
    private lateinit var customTagEntryBinging: CustomTagEntryBinging
    private lateinit var customTagEntryDialog: MaterialDialog
    lateinit var tagsBinding: TagsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        tagsBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_tags,container,false)
        tagsBinding.viewModel = viewModel
        tagsBinding.setLifecycleOwner(this)


        return tagsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //TOOLBAR
        (tagsBinding.toolbar as Toolbar).navigationIcon = resources.getDrawable(R.drawable.ic_humberger_white)
        (tagsBinding.toolbar as Toolbar).setNavigationOnClickListener {
            mainActivity.toggleDrawer()
        }
        (activity as AppCompatActivity).setSupportActionBar(toolbar as Toolbar)
        (activity as AppCompatActivity).supportActionBar?.setTitle(R.string.your_tags)

        fab_add.setOnClickListener {
            customTagEntryBinging.tag = Tag()
            customTagEntryDialog.show()
        }
        presenter.fetchTags()
        setupChipsRecycler()
        setupTagEntryView()
    }

    private fun setupTagEntryView() {
        customTagEntryBinging = DataBindingUtil.inflate(layoutInflater,
                R.layout.custom_view_tag_entry, null, false)

        customTagEntryDialog = MaterialDialog.Builder(context!!)
                .customView(customTagEntryBinging.root, true)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .onPositive { _, _ ->
                    //Get tag in dialog
                    val tag = customTagEntryBinging.tag

                    //Check if update
                    if (tag?.id != 0)
                        presenter.updateTag(tag!!, tagsAdapter.tagList.indexOf(tag))
                    else
                        presenter.addTag(tag)
                }
                .build()


        val fragmentManager = activity?.supportFragmentManager

        customTagEntryBinging.primaryColorView.setOnClickListener {

            val colors : IntArray = IntArray(resources.getStringArray(R.array.tag_colors).size)
            for ( (index,i) in resources.getStringArray(R.array.tag_colors).withIndex()){
                colors[index] = Color.parseColor(i)
            }
            SpectrumDialog.Builder(context!!)
                    .setOnColorSelectedListener { positiveResult, color ->
                        if (positiveResult)
                            customTagEntryBinging.tag?.color = color
                    }
                    .setColors(colors)
                    .setSelectedColor(customTagEntryBinging.tag?.color!!)
                    .setTitle(R.string.pick_a_color)
                    .build()
                    .show(fragmentManager,"ColorPicker")
        }
    }

    private fun setupChipsRecycler() {
        val chipsLayoutManager = ChipsLayoutManager.newBuilder(context)
                .setMaxViewsInRow(3)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .build()
        chips_recycler_view.layoutManager = chipsLayoutManager
        chips_recycler_view.addItemDecoration(
                SpacingItemDecoration(resources.getDimensionPixelOffset(R.dimen.tag_spacing),
                        resources.getDimensionPixelOffset(R.dimen.tag_spacing)))

        tagsAdapter.onTagClickedListener = object : TagsAdapter.OnTagClickedListener {
            override fun onTagClicked(tag: Tag) {
                customTagEntryBinging.tag = tag
                customTagEntryDialog.show()
            }
        }
        tagsAdapter.onTagDeletedListener = object : TagsAdapter.OnTagDeletedListener {
            override fun onTagDeleted(tag: Tag) {
                presenter.deleteTag(tag)
            }
        }
        chips_recycler_view.adapter = tagsAdapter
    }

}
