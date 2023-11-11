package hu.ait.andwallet.screen

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Snackbar
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import hu.ait.andwallet.R
import hu.ait.andwallet.data.MoneyItem
import hu.ait.andwallet.data.MoneyType
import java.util.Date
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoneyListScreen(
    modifier: Modifier = Modifier,
    moneyViewModel: MoneyListViewModel = viewModel(),
    onNavigateToSummary: (Int, Int) -> Unit
) {
    var moneyTitle by rememberSaveable {
        mutableStateOf("")
    }
    var moneyAmount by rememberSaveable {
        mutableStateOf("")
    }
    var moneyIncome by rememberSaveable {
        mutableStateOf(false)
    }

    var errorMessage by remember {
        mutableStateOf<String?>(null)
    }

    Column(
        modifier = modifier.padding(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                value = moneyTitle,
                onValueChange = { moneyTitle = it },
                label = { Text(text = stringResource(R.string.title)) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                value = moneyAmount,
                onValueChange = { moneyAmount = it },
                label = { Text(text = stringResource(R.string.amount_in)) }
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = moneyIncome,
                onCheckedChange = { moneyIncome = it }
            )
            Text(text = stringResource(R.string.income))
        }

        Row {
            Button(onClick = {
                if (moneyTitle.isNotEmpty() && moneyAmount.isNotEmpty()) {
                    val moneyType = if (moneyIncome) MoneyType.INCOME else MoneyType.EXPENSE

                    moneyViewModel.addToMoneyList(
                        MoneyItem(
                            moneyTitle.toString(),
                            moneyAmount.toInt(),
                            moneyType,
                            false
                        )
                    )
                    moneyTitle = ""
                    moneyAmount = ""
                    moneyIncome = false
                } else {
                    val emptyField = if (moneyTitle.isEmpty()) "Title" else "Amount"
                    errorMessage = "$emptyField is empty"
                }
            }) {
                Text(text = stringResource(R.string.save))
            }
            Button(onClick = {
                onNavigateToSummary(
                    moneyViewModel.getExpense(),
                    moneyViewModel.getIncome()
                )
            }) {
                Text(text = stringResource(R.string.summary))
            }

            Button(onClick = {
                moneyViewModel.clearAllItems()
            }) {
                Text(text = stringResource(R.string.delete_all))
            }
        }

        errorMessage?.let { message ->
            Snackbar(
                modifier = Modifier.padding(16.dp),
                action = {
                    TextButton(onClick = { errorMessage = null }) {
                        Text(text = stringResource(R.string.dismiss))
                    }
                }
            ) {
                Text(text = message)
            }
        }

        if (moneyViewModel.getAllItems().isEmpty())
            Text(text = stringResource(R.string.no_items))
        else {
            LazyColumn(modifier = Modifier.fillMaxHeight()) {
                items(moneyViewModel.getAllItems()) { moneyItem ->
                    MoneyCard(moneyItem = moneyItem,
                        onRemoveItem = { moneyViewModel.removeItem(moneyItem) },
                        onMoneyCheckChange = { checkState ->
                            val moneyType = if (checkState) MoneyType.INCOME else MoneyType.EXPENSE

                            val updatedMoneyItem = moneyItem.copy(type = moneyType)

                            moneyViewModel.editMoneyItem(moneyItem, updatedMoneyItem)
                        },
                        onEditItem = { editedMoneyItem ->
                            moneyViewModel.editMoneyItem(moneyItem, editedMoneyItem)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MoneyCard(
    moneyItem: MoneyItem,
    onMoneyCheckChange: (Boolean) -> Unit = {},
    onRemoveItem: () -> Unit = {},
    onEditItem: (MoneyItem) -> Unit = {}
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = Modifier.padding(5.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = moneyItem.type.getIcon()),
                contentDescription = stringResource(R.string.income),
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 10.dp)
            )
            Column{
                Text(moneyItem.title, modifier = Modifier.fillMaxWidth(0.2f))
                Text(text = stringResource(R.string.dollar) + moneyItem.amount.toString(), modifier = Modifier.fillMaxWidth(0.2f))
            }
            Spacer(modifier = Modifier.fillMaxSize(0.55f))

            Spacer(modifier = Modifier.width(10.dp))
            Icon(
                imageVector = Icons.Filled.Delete,
                contentDescription = stringResource(R.string.delete),
                modifier = Modifier.clickable {
                    onRemoveItem()
                },
                tint = Color.Red
            )
        }
    }
}