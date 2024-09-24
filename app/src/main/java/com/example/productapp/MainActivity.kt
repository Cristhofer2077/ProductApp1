package com.example.productapp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.productapp.ui.theme.ProductAppTheme

class MainActivity : ComponentActivity() {
    private lateinit var dbHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHelper = DatabaseHelper(this)

        setContent {
            ProductAppTheme {
                ProductScreen(dbHelper)
            }
        }
    }
}

@Composable
fun ProductScreen(dbHelper: DatabaseHelper) {
    var code by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }


    val products = remember { mutableStateListOf<Product>() }
    LaunchedEffect(Unit) {
        val cursor = dbHelper.getAllProducts()
        while (cursor.moveToNext()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_ID))
            val code = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CODE))
            val description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_DESCRIPTION))
            val price = cursor.getDouble(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_PRICE))
            products.add(Product(id, code, description, price))
        }
        cursor.close()
    }

    Column(modifier = Modifier.padding(16.dp)) {

        OutlinedTextField(
            value = code,
            onValueChange = { code = it },
            label = { Text("Código del Producto") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Descripción del Producto") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = price,
            onValueChange = { price = it },
            label = { Text("Precio del Producto") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))


        Button(onClick = {
            val inserted = dbHelper.insertProduct(code, description, price.toDoubleOrNull() ?: 0.0)
            if (inserted) {
                products.add(Product(products.size + 1, code, description, price.toDoubleOrNull() ?: 0.0))
            }
        }) {
            Text("Registrar Producto")
        }

        Spacer(modifier = Modifier.height(16.dp))


        LazyColumn {
            items(products) { product ->
                Text("ID: ${product.id}, Código: ${product.code}, Descripción: ${product.description}, Precio: ${product.price}")
            }
        }
    }
}

data class Product(val id: Int, val code: String, val description: String, val price: Double)

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    ProductAppTheme {
        ProductScreen(DatabaseHelper(null))
    }
}
