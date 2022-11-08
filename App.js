/* eslint-disable quotes */
/* eslint-disable react-native/no-inline-styles */
/* eslint-disable react-hooks/exhaustive-deps */
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

  useEffect(() => {
    console.log(Module);
  }, []);

  const start_service_btn = async () => {
    try {
      const data = await Module.start_service();
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

  return (
    <SafeAreaView style={{}}>
      <StatusBar barStyle={'dark-content'} />
      <View style={{flexDirection: 'row', justifyContent: 'space-evenly'}}>
        <TouchableOpacity style={styles.btn} onPress={start_service_btn}>
          <Text>Start Service</Text>
        </TouchableOpacity>
        <TouchableOpacity style={styles.btn} onPress={stop_service_btn}>
          <Text>Start Service</Text>
        </TouchableOpacity>
      </View>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  btn: {
    padding: 20,
    backgroundColor: '#666',
    alignSelf: 'center',
    borderRadius: 10,
    marginVertical: 10,
  },
});

export default App;
