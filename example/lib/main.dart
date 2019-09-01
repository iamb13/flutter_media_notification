import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:flutter_media_notification/flutter_media_notification.dart';

void main() => runApp(MyApp());

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  String status = 'null';

  StreamSubscription mediaSubscription;

  @override
  void initState() {
    super.initState();
    // initPlatformState();
    MediaNotification mediaNotification = new MediaNotification();

    // mediaNotification.setListeners('play', () {
    //   setState(() => status = 'newState');
    // });
    mediaSubscription = mediaNotification.onMediaNotificationStateChanged.listen((s){
      if(s == MediaNotificationState.PLAY) {

      }
      if(s == MediaNotificationState.PAUSE) {

      }
    /// .. . . ......

    });


    MediaNotification.setListener('pause', () {
      setState(() => status = 'pause');
    });

    MediaNotification.setListener('play', () {
      setState(() => status = 'play');
    });

    MediaNotification.setListener('next', () {});

    MediaNotification.setListener('prev', () {});

    MediaNotification.setListener('select', () {});
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  // Future<void> initPlatformState() async {
  //   String platformVersion;
  //   // Platform messages may fail, so we use a try/catch PlatformException.
  //   try {
  //     platformVersion = await MediaNotification.platformVersion;
  //   } on PlatformException {
  //     platformVersion = 'Failed to get platform version.';
  //   }

  //   // If the widget was removed from the tree while the asynchronous platform
  //   // message was in flight, we want to discard the reply rather than calling
  //   // setState to update our non-existent appearance.
  //   if (!mounted) return;

  //   setState(() {
  //     _platformVersion = platformVersion;
  //   });
  // }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: Container(
            child: Column(
            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
            children: <Widget>[
              FlatButton(
                child: Text('Show notification'),
                onPressed: () => MediaNotification.show(
                    title: 'Title', author: 'Song author'),
              ),
              FlatButton(
                child: Text('Update notification'),
                onPressed: () => MediaNotification.show(
                    title: 'New Title',
                    author: 'New Song author',
                    isPlaying: false),
              ),
              FlatButton(
                child: Text('Hide notification'),
                onPressed: MediaNotification.hide,
              ),
              Text('Status: ' + status)
            ],
          ),
          ),
        ),
      ),
    );
  }
}
