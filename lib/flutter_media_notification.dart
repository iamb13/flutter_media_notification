import 'dart:async';

import 'package:flutter/services.dart';
import 'package:meta/meta.dart';

/// Communicates the current state of the audio player.
enum MediaNotificationState {
  /// notification is shown, clicking on [play] icon
  /// [pause] will result in exception.
  PLAY,
  /// Currently playing notification, The user can [pause], [prev] or [next] the
  /// playback by clicking on action btns.
  PAUSE,
  /// Play previous media action
  PREV,
  /// Play next media action
  NEXT,
  /// app is in backgroud, call for select and open by click on media nofiticaion
  ///
  SELECT,
  STOPPED
}

/// [MediaNotification] is a class with methods to show and hide
class MediaNotification {
  static const MethodChannel _channel =
      const MethodChannel('com.mb.flutter_media_notification');
  static Map<String, Function> _listeners = new Map();

  // static Future<String> get platformVersion async {
  //   final String version = await _channel.invokeMethod('getPlatformVersion');
  //   return version;
  // }
  

  final StreamController<MediaNotificationState> _mediaNotificationStateController =
      new StreamController.broadcast();
      
  MediaNotificationState _state = MediaNotificationState.STOPPED;

  /// Stream for subscribing to media notification state change events.
  Stream<MediaNotificationState> get onMediaNotificationStateChanged => _mediaNotificationStateController.stream;
  
  
  MediaNotification() {
      _channel.setMethodCallHandler(_mediaNotificationStateHandler);
  }

  static Future<dynamic> _mediaNotificationHandler(MethodCall methodCall) async {
    _listeners.forEach((event, callback) {
      if (methodCall.method == event) {
        callback();
      }
    });
  }

  /// To show your media notification you have to pass [title] and
  /// [author] of music. If music is pausing you have to set
  /// [isPlaying] false.
  Future<void> showNotification({@required title, @required author, isPlaying = true}) async {
    try {
      final Map<String, dynamic> params = <String, dynamic>{
        'title': title,
        'author': author,
        'play': isPlaying
      };
      await _channel.invokeMethod('show', params);
    } on PlatformException catch (e) {
      print("Failed to show notification: '${e.message}'.");
    }
  }
  
  Future<void> hideNotification() async => await _channel.invokeMethod('hide');
  
  static Future show(
    {@required title, @required author, isPlaying = true}) async {
      
    try {
      final Map<String, dynamic> params = <String, dynamic>{
        'title': title,
        'author': author,
        'play': isPlaying
      };
      
      await _channel.invokeMethod('show', params);
      _channel.setMethodCallHandler(_mediaNotificationHandler);
    } on PlatformException catch (e) {
      print("Failed to show notification: '${e.message}'.");
    }
  }

  /// hide media notifications when its calls
  static Future hide() async {
    try {
      await _channel.invokeMethod('hide');
    } on PlatformException catch (e) {
      print("Failed to hide notification: '${e.message}'.");
    }
  }

  /// Set listener for all action that need to listen of notification.
  static setListener(String event, Function callback) {
    _listeners.addAll({event: callback});
  }

  /// Reports what the player is currently doing.
  // MediaNotificationState get state => _state;

  void setListeners(String event, Function callback) {
    // _listeners.addAll({event: callback});


  }

  Future<dynamic> _mediaNotificationStateHandler(MethodCall call) async {
    // _listeners.forEach((event, callback) {
    //   if (methodCall.method == event) {
    //     callback();
    //   }
    // });
    switch(call.method) {
      case "play":
        _state = MediaNotificationState.PLAY;
        _mediaNotificationStateController.add(MediaNotificationState.PLAY);
        break;
      case "pause":
        _state = MediaNotificationState.PAUSE;
        _mediaNotificationStateController.add(MediaNotificationState.PAUSE);
        break;
      case "prev":
        _state = MediaNotificationState.PREV;
        _mediaNotificationStateController.add(MediaNotificationState.PREV);
        break;
      case "next":
        _state = MediaNotificationState.NEXT;
        _mediaNotificationStateController.add(MediaNotificationState.NEXT);
        break;
      case "select":
        _state = MediaNotificationState.SELECT;
        _mediaNotificationStateController.add(MediaNotificationState.SELECT);
        break;
      default:
        throw new ArgumentError('Unknown method ${call.method} ');
    }
    
  }
  
}
