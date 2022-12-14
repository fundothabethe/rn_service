import React, {useEffect} from 'react';
import {
  NativeModules,
  PermissionsAndroid,
  SafeAreaView,
  StatusBar,
  StyleSheet,
  Text,
  TouchableOpacity,
  View,
} from 'react-native';

const App = () => {
  const {Module} = NativeModules;
  const message = 'my_user_id';

  const start_service_btn = async () => {
    try {
      const data = await Module.start_service(message);
      console.log('Service started ' + data);
    } catch (e) {
      console.log('Error occur ' + e);
    }
  };
  const stop_service_btn = async () => {
    try {
      const data = await Module.stop_service();
      console.log('Service Stopped ' + data);
    } catch (error) {
      console.log(error);
    }
  };

  const get_location = () => {
    try {
      Module.get_location(message, _ => console.log(_));
    } catch (error) {
      console.log(error);
    }
  };

  const service_state = async () => {
    try {
      await Module.is_service_running().then(_ => console.log('State: ' + _));
    } catch (error) {
      console.log(error);
    }
  };

  const stop_location_updates = () => Module.stop_location_updates();

  const permissions = async () => {
    try {
      const granted = await PermissionsAndroid.request(
        PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION,
      );
      if (granted === PermissionsAndroid.RESULTS.GRANTED) {
        ask_for_background_location().then(() => Module.ask_permission());
      } else {
      }
    } catch (err) {
      console.warn(err);
    }
  };

  const ask_for_background_location = async () =>
    await PermissionsAndroid.request(
      PermissionsAndroid.PERMISSIONS.ACCESS_BACKGROUND_LOCATION,
      {
        title: 'Zipi Fleet background location',
        message:
          'We need background location permission to track your route, allow zipi to track all the time by choosing "Allow all the time" on location permission.',
        buttonPositive: 'Ok',
      },
    );

  return (
    <SafeAreaView style={{flex: 1}}>
      <StatusBar barStyle={'dark-content'} />
      <View
        style={{
          flexDirection: 'row',
          justifyContent: 'space-evenly',
          alignItems: 'center',
          flexWrap: 'wrap',
        }}>
        <TouchableOpacity style={styles.btn} onPress={start_service_btn}>
          <Text>Start Service</Text>
        </TouchableOpacity>
        <TouchableOpacity style={styles.btn} onPress={service_state}>
          <Text>Service State</Text>
        </TouchableOpacity>
        <TouchableOpacity style={styles.btn} onPress={get_location}>
          <Text>Get location</Text>
        </TouchableOpacity>
        <TouchableOpacity style={styles.btn} onPress={stop_service_btn}>
          <Text>Stop Service</Text>
        </TouchableOpacity>
        <TouchableOpacity style={styles.btn} onPress={stop_location_updates}>
          <Text>Stop Location Updates</Text>
        </TouchableOpacity>
        <TouchableOpacity style={styles.btn} onPress={permissions}>
          <Text>Request location permission</Text>
        </TouchableOpacity>
      </View>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  btn: {
    padding: 20,
    backgroundColor: '#666',
    borderRadius: 10,
    marginVertical: 10,
  },
});

export default App;
