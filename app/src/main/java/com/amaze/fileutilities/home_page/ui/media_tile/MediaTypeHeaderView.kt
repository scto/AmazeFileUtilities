/*
 * Copyright (C) 2021-2023 Arpit Khurana <arpitkh96@gmail.com>, Vishal Nehra <vishalmeham2@gmail.com>,
 * Emmanuel Messulam<emmanuelbendavid@gmail.com>, Raymond Lai <airwave209gt at gmail.com> and Contributors.
 *
 * This file is part of Amaze File Utilities.
 *
 * Amaze File Utilities is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.amaze.fileutilities.home_page.ui.media_tile

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.Button
import android.widget.FrameLayout
import android.widget.HorizontalScrollView
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.edit
import androidx.core.graphics.ColorUtils
import androidx.mediarouter.app.MediaRouteButton
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.amaze.fileutilities.R
import com.amaze.fileutilities.home_page.ui.files.MediaFileAdapter
import com.amaze.fileutilities.home_page.ui.files.MediaFileListSorter
import com.amaze.fileutilities.utilis.AbstractMediaFilesAdapter
import com.amaze.fileutilities.utilis.PreferencesConstants
import com.amaze.fileutilities.utilis.getAppCommonSharedPreferences
import com.amaze.fileutilities.utilis.hideFade
import com.amaze.fileutilities.utilis.px
import com.amaze.fileutilities.utilis.showFade
import com.google.android.material.progressindicator.LinearProgressIndicator

class MediaTypeHeaderView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    private val accentImageView: ImageView
    private val parentLinearLayout: LinearLayout
    private val typeHeaderParent: RelativeLayout
    private val typeImageView: ImageView
    private val mediaRouteButton: MediaRouteButton
    private val infoLayoutParent: LinearLayout
    private val usedSpaceTextView: TextView
    private val usedTotalSpaceTextView: TextView
    private val progressIndicatorsParent: LinearLayout
    private val mediaProgressIndicator: LinearProgressIndicator
    private val progressPercentTextView: TextView
    private val storageCountsParent: RelativeLayout
    private val itemsCountTextView: TextView
    private val totalMediaFiles: TextView
    private val optionsParentLayout: LinearLayout
    private val optionsItemsScroll: HorizontalScrollView
    private val optionsIndexImage: LinearLayout
    private val optionsSwitchView: LinearLayout
    private val optionsGroupView: LinearLayout
    private val optionsSortView: LinearLayout
    private val optionsListParent: LinearLayout
    private val optionsRecyclerViewParent: FrameLayout
    private val optionsRecyclerView: RecyclerView
    private var linearLayoutManager: LinearLayoutManager = LinearLayoutManager(
        context,
        LinearLayoutManager.HORIZONTAL, false
    )

    init {
        val inflater = context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.media_type_header_layout, this, true)
        accentImageView = getChildAt(0) as ImageView
        parentLinearLayout = getChildAt(2) as LinearLayout
        typeHeaderParent = parentLinearLayout.findViewById(R.id.type_header_parent)
        typeImageView = typeHeaderParent.findViewById(R.id.type_image)
        mediaRouteButton = typeHeaderParent.findViewById(R.id.media_route_button)
        infoLayoutParent = parentLinearLayout.findViewById(R.id.infoLayoutParent)
        optionsParentLayout = parentLinearLayout.findViewById(R.id.optionsParent)
        optionsItemsScroll = parentLinearLayout.findViewById(R.id.options_list_scroll)
        optionsRecyclerViewParent = parentLinearLayout
            .findViewById(R.id.options_recycler_view_parent)
        optionsRecyclerView = optionsRecyclerViewParent.findViewById(R.id.options_recycler_view)
        optionsItemsScroll.isHorizontalScrollBarEnabled = false
        usedSpaceTextView = infoLayoutParent.findViewById(R.id.usedSpaceTextView)
        usedTotalSpaceTextView = infoLayoutParent.findViewById(R.id.usedTotalSpaceTextView)
        progressIndicatorsParent = infoLayoutParent.findViewById(R.id.progressIndicatorsParent)
        mediaProgressIndicator = progressIndicatorsParent.findViewById(R.id.mediaProgress)
        progressPercentTextView = progressIndicatorsParent
            .findViewById(R.id.progressPercentTextView)
        storageCountsParent = infoLayoutParent.findViewById(R.id.storageCountsParent)
        itemsCountTextView = storageCountsParent.findViewById(R.id.itemsCountTextView)
        totalMediaFiles = storageCountsParent.findViewById(R.id.internalStorageTextView)
        optionsIndexImage = optionsParentLayout.findViewById(R.id.index_image)
        optionsSwitchView = optionsParentLayout.findViewById(R.id.switch_view)
        optionsGroupView = optionsParentLayout.findViewById(R.id.group_view)
        optionsSortView = optionsParentLayout.findViewById(R.id.sort_view)
        optionsListParent = optionsItemsScroll.findViewById(R.id.options_list_parent)

//        orientation = VERTICAL
//        gravity = Gravity.CENTER_HORIZONTAL

        // init values
        usedSpaceTextView.text = resources.getString(R.string.used_space)
        usedTotalSpaceTextView.text = resources.getString(R.string.used_total_space)
//        typeImageView.setColorFilter(context.resources.getColor(R.color.white))
        progressPercentTextView.text = "--"
    }

    fun setProgress(mediaTypeContent: MediaTypeView.MediaTypeContent) {
        mediaTypeContent.run {
            usedSpaceTextView.text = resources.getString(
                R.string.used_space, size
            )
            usedTotalSpaceTextView.text = resources.getString(
                R.string.used_total_space, totalUsedSpace
            )
            progressPercentTextView.text = "$progress %"
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                mediaProgressIndicator.setProgress(progress, true)
            } else {
                mediaProgressIndicator.progress = progress
            }
            itemsCountTextView.text = resources.getString(
                R.string.num_of_files, String.format("%,d", itemsCount)
            )
            totalMediaFiles.text = resources.getString(
                R.string.media_files_subs, String.format("%,d", totalItemsCount)
            )
        }
        invalidate()
    }

    fun setTypeImageSrc(imageRes: Drawable) {
//        typeImageView.setImageDrawable(imageRes)
    }

    fun setAccentImageSrc(accentImage: Drawable) {
        accentImageView.setImageDrawable(accentImage)
        /*Glide.with(context).load(accentImage)
            .centerCrop()
            .transform(CenterCrop(), RoundedCorners(24.px.toInt()))
            .fallback(R.drawable.ic_outline_broken_image_24)
            .placeholder(R.drawable.ic_outline_image_32).into(accentImageView)*/
    }

    fun getMediaRouteButton(): MediaRouteButton {
        return mediaRouteButton
    }

    fun setHeaderColor(headerColor: Int, headerRes: Int) {
//        setBackgroundResource(headerRes)
//        setBackgroundResource(R.drawable.background_curved)
        mediaProgressIndicator.trackColor = ColorUtils.blendARGB(
            headerColor,
            Color.BLACK, .2f
        )
    }

    fun initOptionsItems(
        optionsMenuSelected: MediaFileAdapter.OptionsMenuSelected,
        headerListItems: MutableList<AbstractMediaFilesAdapter.ListItem>,
        sortingPreference: MediaFileListSorter.SortingPreference,
        mediaListType: Int,
        reloadListCallback: () -> Unit
    ) {
        val adapter = MediaTypeViewOptionsListAdapter(
            context, headerListItems,
            optionsMenuSelected
        )
        optionsRecyclerView.layoutManager = linearLayoutManager
        optionsRecyclerView.adapter = adapter
        clickOptionsIndex()
        val sharedPreferences = context.getAppCommonSharedPreferences()
        optionsIndexImage.setOnClickListener {
            clickOptionsIndex()
        }
        optionsSwitchView.setOnClickListener {
            clickOptionsSwitchView(optionsMenuSelected, sharedPreferences, mediaListType)
        }
        optionsGroupView.setOnClickListener {
            clickOptionsGroupView(
                optionsMenuSelected, sharedPreferences, sortingPreference,
                mediaListType,
                reloadListCallback
            )
        }
        optionsSortView.setOnClickListener {
            clickOptionsSortView(
                optionsMenuSelected, sharedPreferences, sortingPreference,
                mediaListType,
                reloadListCallback
            )
        }
    }

    private fun clickOptionsIndex() {
        clearOptionItemsBackgrounds()
        optionsItemsScroll.hideFade(200)
        optionsRecyclerViewParent.showFade(300)
        optionsIndexImage.background = resources.getDrawable(R.drawable.button_selected_dark)
    }

    private fun clickOptionsSwitchView(
        optionsMenuSelected: MediaFileAdapter.OptionsMenuSelected,
        sharedPreferences: SharedPreferences,
        mediaListType: Int
    ) {
        clearOptionItemsBackgrounds()
        optionsRecyclerViewParent.hideFade(300)
        optionsItemsScroll.showFade(200)
        optionsSwitchView.background = resources.getDrawable(R.drawable.button_selected_dark)
        val listViewButton: Button
        val gridViewButton: Button
        if (sharedPreferences.getBoolean(
                "${mediaListType}_${PreferencesConstants.KEY_MEDIA_LIST_TYPE}",
                PreferencesConstants.DEFAULT_MEDIA_LIST_TYPE
            )
        ) {
            listViewButton = getSelectedTextButton(resources.getString(R.string.list_view))
            gridViewButton = getUnSelectedTextButton(resources.getString(R.string.grid_view))
        } else {
            listViewButton = getUnSelectedTextButton(resources.getString(R.string.list_view))
            gridViewButton = getSelectedTextButton(resources.getString(R.string.grid_view))
        }

        listViewButton.setOnClickListener {
            setSelectButton(listViewButton)
            setUnSelectButton(gridViewButton)
            sharedPreferences.edit {
                this.putBoolean(
                    "${mediaListType}_${PreferencesConstants.KEY_MEDIA_LIST_TYPE}",
                    true
                ).apply()
            }
            optionsMenuSelected.switchView(true)
        }
        gridViewButton.setOnClickListener {
            setSelectButton(gridViewButton)
            setUnSelectButton(listViewButton)
            sharedPreferences.edit {
                this.putBoolean(
                    "${mediaListType}_${PreferencesConstants.KEY_MEDIA_LIST_TYPE}",
                    false
                ).apply()
            }
            optionsMenuSelected.switchView(false)
        }

        optionsListParent.addView(listViewButton)
        optionsListParent.addView(gridViewButton)
    }

    private fun clickOptionsGroupView(
        optionsMenuSelected: MediaFileAdapter.OptionsMenuSelected,
        sharedPreferences: SharedPreferences,
        sortingPreference: MediaFileListSorter.SortingPreference,
        mediaListType: Int,
        reloadListCallback: () -> Unit
    ) {
        clearOptionItemsBackgrounds()
        optionsRecyclerViewParent.hideFade(300)
        optionsItemsScroll.showFade(200)
        optionsGroupView.background = resources.getDrawable(R.drawable.button_selected_dark)

        val buttonsList = ArrayList<Button>()
        var isAsc = sharedPreferences
            .getBoolean(
                MediaFileListSorter.SortingPreference.getIsGroupByAscKey(mediaListType),
                PreferencesConstants.DEFAULT_MEDIA_LIST_GROUP_BY_ASC
            )
        MediaFileListSorter.GROUP_BY_MEDIA_TYPE_MAP[mediaListType]?.forEach {
            groupByType ->
            val button = if (groupByType == sortingPreference.groupBy) {
                getSelectedTextButton(
                    MediaFileListSorter.getGroupNameByType(
                        groupByType,
                        isAsc,
                        resources
                    )
                )
            } else {
                getUnSelectedTextButton(
                    // unselected buttons will always show ascending
                    MediaFileListSorter.getGroupNameByType(
                        groupByType,
                        true,
                        resources
                    )
                )
            }
            button.setOnClickListener {
                buttonsList.forEach {
                    allButtons ->
                    setUnSelectButton(allButtons)
                }
                setSelectButton(button)

                sharedPreferences.edit {
                    this.putInt(
                        MediaFileListSorter.SortingPreference.getGroupByKey(mediaListType),
                        groupByType
                    ).commit()
                }
                sortingPreference.groupBy = groupByType
                isAsc = !isAsc
                sharedPreferences.edit().putBoolean(
                    MediaFileListSorter.SortingPreference.getIsGroupByAscKey(mediaListType),
                    isAsc
                ).commit()
                sortingPreference.isGroupByAsc = isAsc
                optionsMenuSelected.groupBy(sortingPreference)
                reloadListCallback()
            }
            buttonsList.add(button)
        }
        buttonsList.forEach {
            optionsListParent.addView(it)
        }
    }

    private fun clickOptionsSortView(
        optionsMenuSelected: MediaFileAdapter.OptionsMenuSelected,
        sharedPreferences: SharedPreferences,
        sortingPreference: MediaFileListSorter.SortingPreference,
        mediaListType: Int,
        reloadListCallback: () -> Unit
    ) {
        clearOptionItemsBackgrounds()
        optionsRecyclerViewParent.hideFade(300)
        optionsItemsScroll.showFade(200)
        optionsSortView.background = resources.getDrawable(R.drawable.button_selected_dark)

        val buttonsList = ArrayList<Button>()
        var isAsc = sharedPreferences
            .getBoolean(
                MediaFileListSorter.SortingPreference.getIsSortByAscKey(mediaListType),
                PreferencesConstants.DEFAULT_MEDIA_LIST_SORT_BY_ASC
            )
        MediaFileListSorter.SORT_BY_MEDIA_TYPE_MAP[mediaListType]?.forEach {
            sortByType ->
            val button = if (sortByType == sortingPreference.sortBy) {
                getSelectedTextButton(
                    MediaFileListSorter.getSortNameByType(
                        sortByType,
                        isAsc,
                        resources
                    )
                )
            } else {
                getUnSelectedTextButton(
                    // unselected buttons will always show ascending
                    MediaFileListSorter.getSortNameByType(
                        sortByType,
                        true,
                        resources
                    )
                )
            }
            button.setOnClickListener {
                buttonsList.forEach {
                    allButtons ->
                    setUnSelectButton(allButtons)
                }
                setSelectButton(button)

                sharedPreferences.edit {
                    this.putInt(
                        MediaFileListSorter.SortingPreference.getSortByKey(mediaListType),
                        sortByType
                    ).apply()
                }
                sortingPreference.sortBy = sortByType
                isAsc = !isAsc
                sharedPreferences.edit().putBoolean(
                    MediaFileListSorter.SortingPreference.getIsSortByAscKey(mediaListType),
                    isAsc
                ).apply()
                sortingPreference.isSortByAsc = isAsc
                optionsMenuSelected.sortBy(sortingPreference)
                reloadListCallback()
            }
            buttonsList.add(button)
        }
        buttonsList.forEach {
            optionsListParent.addView(it)
        }
    }

    private fun getSelectedTextButton(text: String): Button {
        val button = Button(context)
        setSelectButton(button)
        setParams(button)
        button.text = text
        return button
    }

    private fun getUnSelectedTextButton(text: String): Button {
        val button = Button(context)
        setUnSelectButton(button)
        setParams(button)
        button.text = text
        return button
    }

    private fun setParams(button: Button) {
        val params = LayoutParams(
            LayoutParams.WRAP_CONTENT,
            LayoutParams.WRAP_CONTENT
        )
        params.leftMargin = 16.px.toInt()
        button.layoutParams = params
    }

    private fun setSelectButton(button: Button) {
        button.background = resources.getDrawable(R.drawable.button_curved_selected)
        button.setTextColor(resources.getColor(R.color.navy_blue))
    }

    private fun setUnSelectButton(button: Button) {
        button.background = resources.getDrawable(R.drawable.button_curved_unselected)
        button.setTextColor(resources.getColor(R.color.white))
    }

    private fun clearOptionItemsBackgrounds() {
        optionsIndexImage.background = null
        optionsListParent.removeAllViews()
        optionsSwitchView.background = null
        optionsGroupView.background = null
        optionsSortView.background = null
    }
}
