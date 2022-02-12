package com.kitmak.composetruckandroid.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import com.kitmak.composetruckandroid.R
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.rememberCameraPositionState
import com.kitmak.composetruckandroid.ui.theme.Shapes
import com.kitmak.composetruckandroid.vm.MainViewModel
import com.kitmak.network.remote.responses.TruckLocation
import com.kitmak.network.remote.responses.TruckRoute
import com.kitmak.network.remote.responses.safe
import com.kitmak.repo.TruckResult
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MainScreen(vm: MainViewModel) {
    val scaffoldState = rememberBottomSheetScaffoldState()
    var truckLocation by remember { mutableStateOf<TruckResult<List<TruckLocation>>>(TruckResult.Loading) }
    var currentTruckState by remember { mutableStateOf<TruckLocation?>(null) }

    LaunchedEffect(true) {
        vm.fetchTruckLocation().collectLatest {
            truckLocation = it
        }
    }
    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        drawerGesturesEnabled = false,
        floatingActionButtonPosition = FabPosition.End,
        sheetPeekHeight = 178.dp,
        sheetShape = RoundedCornerShape(topEnd = 32.dp, topStart = 32.dp),
        topBar = { TopBar(scaffoldState) },
        sheetContent = {
            Sheet(
                scaffoldState = scaffoldState,
                truckLocation = truckLocation,
                currentTruckState = currentTruckState,
                onLoadRouteClick = { vm.findRouteById(it) },
            )
        },
        floatingActionButton = { Fab(scaffoldState) },
        drawerContent = { Drawer(scaffoldState) }) { innerPadding ->
        TruckMap(
            modifier = Modifier.fillMaxSize(),
            trucks = truckLocation
        ) {
            currentTruckState = it
            true
        }

    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Sheet(
    scaffoldState: BottomSheetScaffoldState,
    truckLocation: TruckResult<List<TruckLocation>>,
    currentTruckState: TruckLocation?,
    onLoadRouteClick: suspend (String) -> List<TruckRoute>
) {
    val scope = rememberCoroutineScope()
    var routeState by remember { mutableStateOf<List<TruckRoute>>(emptyList()) }

    val peakHeight = 158.dp
    currentTruckState?.safe()?.let { truck ->
        when (truckLocation) {
            is TruckResult.Error -> TODO()
            TruckResult.Loading -> CircularProgressIndicator()
            is TruckResult.Success -> {
                Column(
                    Modifier
                        .padding(start = 32.dp, end = 32.dp, top = 32.dp)
                        .height(peakHeight)
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(style = typography.h5, text = "${truck.cityName} ${truck.car}")
                    Text(style = typography.h6, text = truck.location)
                    Text(style = typography.h6, text = truck.readableTime)
                    Spacer(Modifier.height(8.dp))
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Chip(
                            onClick = { /* Do something! */ },
                            border = ChipDefaults.outlinedBorder,
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.Favorite,
                                    contentDescription = "Localized description"
                                )
                            }
                        ) {
                            Text("Bookmark")
                        }
                        Chip(
                            onClick = { /* Do something! */ },
                            border = ChipDefaults.outlinedBorder,
                            leadingIcon = {
                                Icon(
                                    Icons.Filled.Share,
                                    contentDescription = "Localized description"
                                )
                            }
                        ) {
                            Text("Share")
                        }
                        Chip(
                            onClick = { /* Do something! */ },
                            border = ChipDefaults.outlinedBorder,
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Place,
                                    contentDescription = "Localized description"
                                )
                            }
                        ) {
                            Text("Open in")
                        }
                    }
                }

                Card(
                    onClick = {},
                    border = BorderStroke(1.dp, Color.Gray),
                    elevation = 0.dp,
                    shape = Shapes.large,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 32.dp, end = 32.dp, bottom = 32.dp),
                ) {
                    Column(
                        Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top,
                    ) {
                        Text(style = typography.body1, text = "latitude and longitude:")
                        Spacer(Modifier.height(4.dp))
                        Text(style = typography.body2, text = truck.latitude.toString())
                        Text(style = typography.body2, text = truck.longitude.toString())
                    }
                }

                Card(

                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    Button(onClick = {
                        scope.launch {
                            routeState = onLoadRouteClick(truck.lineId)
                        }
                    }) {
                        Text(style = typography.body1, text = routeState.toString())

                    }
                }
            }
        }


    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Fab(scaffoldState: BottomSheetScaffoldState) {
    val scope = rememberCoroutineScope()
    var clickCount by remember { mutableStateOf(0) }
    FloatingActionButton(
        onClick = {
            // show snackbar as a suspend function
            scope.launch {
                scaffoldState.snackbarHostState.showSnackbar("Snackbar #${++clickCount}")
            }
        }
    ) {
        Icon(Icons.Default.LocationOn, contentDescription = "Localized description")
    }

}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun TopBar(scaffoldState: BottomSheetScaffoldState) {
    val scope = rememberCoroutineScope()
    TopAppBar(
        title = { Text("Bottom sheet scaffold") },
        navigationIcon = {
            IconButton(onClick = { scope.launch { scaffoldState.drawerState.open() } }) {
                Icon(Icons.Default.Menu, contentDescription = "Localized description")
            }
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Drawer(scaffoldState: BottomSheetScaffoldState) {
    val scope = rememberCoroutineScope()
    Column(
        Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Drawer content")
        Spacer(Modifier.height(20.dp))
        Button(onClick = { scope.launch { scaffoldState.drawerState.close() } }) {
            Text("Click to close drawer")
        }
    }
}

@Composable
fun TruckMap(
    modifier: Modifier = Modifier,
    trucks: TruckResult<List<TruckLocation>>,
    onClick: (TruckLocation) -> Boolean
) {

    val singapore = LatLng(1.35, 103.87)
    val cameraPositionState: CameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(singapore, 11f)
    }
    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
    ) {
        when (trucks) {
            is TruckResult.Error -> TODO()
            TruckResult.Loading -> {}
            is TruckResult.Success -> {
                val safeTrucks = trucks.value.map { it.safe() }
                val latLngBuilder = LatLngBounds.Builder()
                safeTrucks.filterNotNull().forEach {
                    latLngBuilder.include(LatLng(it.latitude, it.longitude))
                }
                trucks.value.forEach { truck ->
                    truck.latitudeDouble?.let { lat ->
                        truck.longitudeDouble?.let { lng ->
                            Marker(
                                position = LatLng(lat, lng),
                                icon = BitmapDescriptorFactory.fromResource(R.drawable.pickup_truck),
                                onClick = {
                                    onClick(truck)
                                }
                            )
                        }
                    }
                }
                cameraPositionState.move(
                    CameraUpdateFactory.newLatLngBounds((latLngBuilder.build()), 0)
                )
            }
        }

    }
}