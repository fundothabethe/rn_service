/* eslint-disable react-hooks/exhaustive-deps */
/* eslint-disable react-native/no-inline-styles */
import React, {useEffect} from 'react';
import {
  NativeModules,
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
      Module.get_location(message, _ => console.log('Getting ' + _));
    } catch (error) {
      console.log(error);
    }
  };
  const creating_sql_db = () => {};

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
        <TouchableOpacity style={styles.btn} onPress={get_location}>
          <Text>Get location</Text>
        </TouchableOpacity>
        <TouchableOpacity style={styles.btn} onPress={stop_service_btn}>
          <Text>Stop Service</Text>
        </TouchableOpacity>
        <TouchableOpacity style={styles.btn} onPress={creating_sql_db}>
          <Text>Create db</Text>
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
