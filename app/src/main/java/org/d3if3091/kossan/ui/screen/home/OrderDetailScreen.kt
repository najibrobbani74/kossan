package org.d3if3091.kossan.ui.screen.home

import android.content.res.Configuration
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if3091.kossan.R
import org.d3if3091.kossan.database.KossanDb
import org.d3if3091.kossan.ui.theme.KossanTheme
import org.d3if3091.kossan.util.ViewModelFactory
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Locale

const val KEY_ID_ORDER = "idOrder"

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailScreen(navHostController: NavHostController, id: Long? = null) {
    val context = LocalContext.current
    val db = KossanDb.getInstance(context)
    val factory = ViewModelFactory(db.orderDao)
    val viewModel: OrderDetailViewModel = viewModel(factory = factory)

    var customerName by remember { mutableStateOf("") }
    var room by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    var showDialog by remember { mutableStateOf(false) }

    var endTimePickerState = rememberDatePickerState()
    var showEndTimePicker by remember { mutableStateOf(false) }

    var startTimePickerState = rememberDatePickerState()

    var showStartTimePicker by remember { mutableStateOf(false) }

    val dateFormatter = SimpleDateFormat("dd MMMM yyyy", Locale.US)


    LaunchedEffect(true) {
        if (id == null) return@LaunchedEffect
        val data = viewModel.getOrderById(id) ?: return@LaunchedEffect
        customerName = data.customer_name
        room = data.room
        startTime = data.start_time
        endTime = data.end_time
        price = data.total_price.toString()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navHostController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_icon),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                title = {
                    if (id == null)
                        Text(text = stringResource(id = R.string.add_order_title))
                    else
                        Text(text = stringResource(id = R.string.edit_order_title))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    IconButton(onClick = {
                        if (customerName == "" || room == "" || startTime == "" || endTime == "" || price == "") {
                            Toast.makeText(context, R.string.empty_form_message, Toast.LENGTH_LONG)
                                .show()
                            return@IconButton
                        }

                        if (id == null) {
                            viewModel.insert(customerName, room, startTime, endTime, price)
                        } else {
                            viewModel.update(id, customerName, room, startTime, endTime, price)
                        }
                        navHostController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Outlined.Check,
                            contentDescription = stringResource(R.string.save_text),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                    if (id != null) {
                        DeleteAction { showDialog = true }
                        DisplayAlertDialog(
                            openDialog = showDialog,
                            onDismissRequest = { showDialog = false }) {
                            showDialog = false
                            viewModel.delete(id)
                            navHostController.popBackStack()
                        }
                    }
                }
            )
        }
    ) { padding ->
        FormOrder(
            customerName = customerName,
            onCustomerNameChange = { customerName = it },
            room = room,
            onRoomChange = { room = it },
            startTime = startTime,
            onStartTimeChange = { startTime = it },
            endTime = endTime,
            onEndTimeChange = { endTime = it },
            price = price,
            onPriceChange = { price = it },
            endTimePickerState = endTimePickerState,
            showEndTimePicker = showEndTimePicker,
            onShowEndTimePicker = { showEndTimePicker = it },
            startTimePickerState = startTimePickerState,
            showStartTimePicker = showStartTimePicker,
            onShowStartTimePicker = { showStartTimePicker = it },
            modifier = Modifier.padding(padding)
        )
        if (showStartTimePicker) {
            DatePickerDialog(

                onDismissRequest = { /*TODO*/ },
                confirmButton = {
                    TextButton(
                        onClick = {
                            startTime = if (startTimePickerState.selectedDateMillis != null) {
                                dateFormatter.format((Calendar.getInstance().apply {
                                    timeInMillis = startTimePickerState.selectedDateMillis!!
                                }).time)
                            } else {
                                ""
                            }
                            showStartTimePicker = false
                        }
                    ) { Text(stringResource(id = R.string.select_text)) }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showStartTimePicker = false
                        }
                    ) { Text(stringResource(id = R.string.cancel_text)) }
                }) {
                DatePicker(
                    state = startTimePickerState,
                    headline = null,
                    showModeToggle = false,
                    title = null
                )
            }
        }
        if (showEndTimePicker) {
            DatePickerDialog(
                onDismissRequest = { /*TODO*/ },
                confirmButton = {
                    TextButton(
                        onClick = {
                            endTime = if (endTimePickerState.selectedDateMillis != null) {
                                dateFormatter.format((Calendar.getInstance().apply {
                                    timeInMillis = endTimePickerState.selectedDateMillis!!
                                }).time)
                            } else {
                                ""
                            }
                            showEndTimePicker = false
                        }
                    ) { Text(stringResource(id = R.string.select_text)) }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showEndTimePicker = false
                        }
                    ) { Text(stringResource(id = R.string.cancel_text)) }
                }) {

                DatePicker(
                    state = endTimePickerState,
                    headline = null,
                    showModeToggle = false,
                    title = null
                )

            }
        }
    }
}

@Composable
fun DeleteAction(delete: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    IconButton(onClick = { expanded = true }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(R.string.more_text),
            tint = MaterialTheme.colorScheme.primary
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = {
                    Text(text = stringResource(id = R.string.delete_text))
                },
                onClick = {
                    expanded = false
                    delete()
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormOrder(
    customerName: String, onCustomerNameChange: (String) -> Unit,
    room: String, onRoomChange: (String) -> Unit,
    startTime: String, onStartTimeChange: (String) -> Unit,
    endTime: String, onEndTimeChange: (String) -> Unit,
    price: String, onPriceChange: (String) -> Unit,
    endTimePickerState: DatePickerState,
    showEndTimePicker: Boolean,
    onShowEndTimePicker: (Boolean) -> Unit,
    startTimePickerState: DatePickerState,
    showStartTimePicker: Boolean,
    onShowStartTimePicker: (Boolean) -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = customerName,
            onValueChange = { onCustomerNameChange(it) },
            label = { Text(text = stringResource(R.string.customer_name_text)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = room,
            onValueChange = { onRoomChange(it) },
            label = { Text(text = stringResource(R.string.room_text)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )

        val sourceStartTimeInput = remember { MutableInteractionSource() }
        if (sourceStartTimeInput.collectIsPressedAsState().value) {
            onShowStartTimePicker(true)
        }
        OutlinedTextField(
            value = startTime,
            readOnly = true,
            onValueChange = {},
            label = { Text(text = stringResource(id = R.string.start_time_text)) },
            interactionSource = sourceStartTimeInput,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_calendar_month_24),
                    contentDescription = stringResource(id = R.string.calendar_icon)
                )
            }
        )
        val sourceEndTimeInput = remember { MutableInteractionSource() }
        if (sourceEndTimeInput.collectIsPressedAsState().value) {
            onShowEndTimePicker(true)
        }
        OutlinedTextField(
            value = endTime,
            readOnly = true,
            onValueChange = {},
            label = { Text(text = stringResource(id = R.string.end_time_text)) },
            interactionSource = sourceEndTimeInput,
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.baseline_calendar_month_24),
                    contentDescription = stringResource(id = R.string.calendar_icon)
                )
            }
        )
        OutlinedTextField(
            value = price,
            onValueChange = { onPriceChange(it) },
            label = { Text(text = stringResource(R.string.total_price_text)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done,
                capitalization = KeyboardCapitalization.Sentences
            ),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun OrderDetailScreenPreview() {
    KossanTheme {
        OrderDetailScreen(rememberNavController())
    }
}