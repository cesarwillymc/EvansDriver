package com.evans.technologies.conductor.data.remote.request

data class DriverToUser(        var userId:String,
                                var title:String,
                                var message:String,
                                var response:String,
                                var chatId:String,
                                var tripId:String)