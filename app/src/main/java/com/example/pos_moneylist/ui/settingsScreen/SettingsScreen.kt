package com.example.pos_moneylist.ui.settingsScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pos_moneylist.Controller
import com.example.pos_moneylist.R
import com.example.pos_moneylist.data.productList.Product
import com.example.pos_moneylist.ui.ViewModelProvider
import com.example.pos_moneylist.ui.addProductScreen.AddProductDialog
import com.example.pos_moneylist.ui.productDetailsAndEditScreen.ProductDetailsAndEditDialog

@Composable
fun SettingsScreen(
    settingsScreenViewModel: SettingsScreenViewModel = viewModel(factory = ViewModelProvider.Factory),
) {

    val productList = remember { settingsScreenViewModel.productList.productList }
    var productDetails: Product = remember { Product("No product", 0.00f, Color.Black) }

    var showProductList: Boolean by remember { mutableStateOf(false) }
    var showAddProductScreen: Boolean by remember { mutableStateOf(false) }
    var showProductDetailsScreen: Boolean by remember { mutableStateOf(false) }

    showProductList = productList.size != 0


    Scaffold(
        floatingActionButton = {
            IconButton(onClick = { showAddProductScreen = true }) {
                Icon(
                    Icons.TwoTone.Add, contentDescription = "Add button"
                )
            }
        }) { innerPadding ->
        if (showProductList) {
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
            ) {
                LazyColumn(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .background(Color.White),
                    horizontalAlignment = Alignment.Start,
                ) {
                    items(items = productList, key = { it.name }) { product: Product ->
                        ListItem(headlineContent = {
                            Text(
                                modifier = Modifier.clickable {
                                    showProductDetailsScreen = true
                                    productDetails = product
                                },
                                text = product.name,
                                fontSize = 40.sp
                            )
                        })

                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth(),
                contentAlignment = Alignment.TopCenter
            ) {
                Text(
                    text = stringResource(R.string.empty_list), fontSize = 40.sp
                )
            }
        }
        if (showAddProductScreen) {
            AddProductDialog(
                onDismissRequest = { showAddProductScreen = false },
                onCancel = { showAddProductScreen = false },
                onConfirm = { product ->
                    settingsScreenViewModel.addProduct(product = product)
                    showAddProductScreen = false
                },
                onNameChange = { product -> settingsScreenViewModel.contains(product = product) })
        }

        if (showProductDetailsScreen) {
            ProductDetailsAndEditDialog(
                onDismissRequest = { showProductDetailsScreen = false },
                onCancel = { showProductDetailsScreen = false },
                onConfirm = {
                    Controller.saveProductList()
                    showProductDetailsScreen = false
                },
                onNameChange = { product -> settingsScreenViewModel.contains(product = product) },
                onDelete = { product ->
                    settingsScreenViewModel.remove(product)
                    Controller.saveProductList()
                    showProductDetailsScreen = false
                },
                product = productDetails
            )
        }
    }
}