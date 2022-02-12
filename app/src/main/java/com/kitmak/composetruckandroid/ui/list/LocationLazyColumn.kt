package com.kitmak.composetruckandroid.ui.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kitmak.network.remote.responses.TruckLocation

@Composable
fun LocationLazyColumn(truckLocation: List<TruckLocation>, onClick: (TruckLocation) -> Unit) {
    if (truckLocation.isEmpty()){
        EmptyTruckList()
    } else {
        LazyColumn(
            contentPadding = PaddingValues(all = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(truckLocation) { where ->
                LocationCard(where) { onClick(where) }
            }
        }
    }
}

@Composable
fun LocationCard(truckLocation: TruckLocation, onClick: (TruckLocation) -> Unit) {
    Card(
        modifier = Modifier
            .width(300.dp)
            .wrapContentHeight()
            .clickable { onClick(truckLocation) },
        elevation = 10.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "清運路線編號: ${truckLocation.lineId}")
            Text(text = "車牌號碼: ${truckLocation.car}")
            Text(text = "行政區名稱: ${truckLocation.cityName}")
            Text(text = "所在位址: ${truckLocation.location}")
            Text(text = "回報時間: ${truckLocation.time}")
        }
    }
}

@Composable
fun EmptyTruckList(){
    Box(modifier = Modifier.fillMaxHeight().fillMaxWidth(), contentAlignment = Alignment.Center) {
        Text(text = "No items available \uD83E\uDD7A")
    }
}