package com.example.pos_moneylist.ui.productDetailsAndEditScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.window.Dialog
import com.example.pos_moneylist.R
import com.example.pos_moneylist.data.productList.Product

@Composable
fun ProductDetailsAndEditDialog(
    onDismissRequest: () -> Unit,
    onCancel: () -> Unit,
    onConfirm: () -> Unit,
    onNameChange: (product: Product) -> Boolean,
    onDelete: (product: Product) -> Unit,
    product: Product,
) {

    var productName: String by remember { mutableStateOf(product.name) }
    var productPrice: String by remember { mutableStateOf(product.price.toString()) }
    var productColor: Color by remember { mutableStateOf(product.color) }

    var nameIsValid: Boolean by remember { mutableStateOf(true) }
    var priceIsValid: Boolean by remember { mutableStateOf(true) }
    var changesMade: Boolean by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismissRequest) {
        Box(modifier = Modifier.background(Color.White)) {
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                //Product name
                OutlinedTextField(
                    label = { Text(text = stringResource(R.string.add_product_name)) },
                    value = productName,
                    onValueChange = {
                        nameIsValid =
                            !onNameChange(Product(it, 0.0f, Color.Black)) and it.isNotEmpty()
                        changesMade = true
                        productName = it
                    },
                    colors = if (nameIsValid) {
                        OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.Black)
                    } else {
                        OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.Red)
                    }
                )

                //Product price
                OutlinedTextField(
                    label = { Text(text = stringResource(R.string.add_product_price)) },
                    value = productPrice,
                    onValueChange = {
                        priceIsValid = it.isNotEmpty() and !it.startsWith("-")
                        changesMade = true
                        priceIsValid = try {
                            it.toFloat()
                            true
                        } catch (e: NumberFormatException) {
                            false
                        }
                        productPrice = it
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = if (priceIsValid) {
                        OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.Black)
                    } else {
                        OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.Red)
                    },
                    //visualTransformation = CurrencyAmountVisualTransformation()
                )

                //Product color
                OutlinedTextField(
                    label = { Text(text = stringResource(R.string.color)) },
                    value = productColor.toString(),
                    onValueChange = { /*TODO*/ })

                //Action buttons
                Row(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    //Delete button
                    Button(
                        onClick = { onDelete(product) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
                    ) {
                        Text(text = stringResource(R.string.button_delete))
                    }
                    //Cancel button
                    Button(onClick = onCancel) {
                        Text(text = stringResource(R.string.button_cancel))
                    }
                    //Confirm button
                    Button(
                        enabled = nameIsValid and priceIsValid and changesMade,
                        onClick = {
                            product.name = productName
                            product.price = productPrice.toFloat()
                            product.color = productColor
                            onConfirm()
                        },
                    ) {
                        Text(text = stringResource(R.string.button_confirm))
                    }
                }
            }
        }
    }
}