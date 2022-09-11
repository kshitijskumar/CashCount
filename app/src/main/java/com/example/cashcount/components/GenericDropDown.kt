package com.example.cashcount.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.toSize
import com.example.cashcount.R

@Composable
fun <ITEM_TYPE : DropDownItem>GenericDropDown(
    startingItem: ITEM_TYPE? = null,
    itemsList: List<ITEM_TYPE>,
    onItemSelected: (ITEM_TYPE) -> Unit,
    placeholder: String
) {
    val mExpanded = remember { mutableStateOf(false) }

    val selectedItem = remember {
        mutableStateOf<ITEM_TYPE?>(startingItem)
    }

    val mTextFieldSize = remember { mutableStateOf(Size.Zero) }

    val icon = if (mExpanded.value)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column {

        CashCountOutlinedTextField(
            value = selectedItem.value?.toItemNameString() ?: "",
            onValueChange = {
//                selectedItem.value =  AccountType.stringToAccountType(it)
//                onAccountTypeSelected.invoke(AccountType.stringToAccountType(it))
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { mExpanded.value = !mExpanded.value }
                .onGloballyPositioned { coordinates ->
                    mTextFieldSize.value = coordinates.size.toSize()
                },
            trailingIcon = {
                Icon(icon,"contentDescription",
                    Modifier.clickable { mExpanded.value = !mExpanded.value })
            },
            readOnly = true,
            placeholderText = placeholder
        )

        DropdownMenu(
            expanded = mExpanded.value,
            onDismissRequest = { mExpanded.value = false },
            modifier = Modifier
                .width(with(LocalDensity.current){mTextFieldSize.value.width.toDp()})
        ) {
            itemsList.forEach { label ->
                DropdownMenuItem(onClick = {
                    selectedItem.value = label
                    mExpanded.value = false
                    onItemSelected.invoke(label)
                }) {
                    Text(text = label.toItemNameString())
                }
            }
        }
    }
}

interface DropDownItem {
    fun toItemNameString(): String
}