#include <SoftwareSerial.h>
#include <stdlib.h>
#include <math.h>
#include "HX711.h"

#define BT_RXD 8
#define BT_TXD 7
SoftwareSerial bluetooth(7, 8);

#define calibration_factor -255000
#define DOUT 3
#define CLK 2
HX711 scale(DOUT, CLK);

int num = 1;
int count = 1;



int number_of_digits(int n)
{
    return floor(log10(n)+1);
}

 
void sendCommand(const char * command) {
  bluetooth.write(command);
  Serial.println(command);
  // wait some time
  delay(1000);
  
}



void setup() {
  Serial.begin(9600);
  bluetooth.begin(9600);
  scale.set_scale(calibration_factor);
  scale.tare();
  Serial.println("Readings: ");
}

void loop() {
  float num_f = scale.get_units(); //무게정보 받기
  int num = (int) (num_f * 1000);
  
 
  
  if(num < 0)                      //무게정보 양수 변환
  {                   
    num = -num;
  }

  else if (num == 0){
    num = 1;
  }
  
  if (num != 0){                      //자리수 구하기
    count = number_of_digits(num);
  }
  
  
  //커맨드 스트링 생성 과정
  char command[] = "AT+IBE0";
  
  char str1[10] = "";
  char str2[10] = "";
  
  itoa(num, str2,10);
  
  char zero[2] = "0";
  
  int i = 0;

  //0 추가 과정
  
  while(i<8-count){
    strcat(str1, zero);
    i = i + 1;
    Serial.print("i=");
    Serial.print(i, ",");
    
  }
  
  strcat(str1, str2);
  strcat(command, str1);
  
  bluetooth.write(command);
  Serial.print(" ");
  Serial.println(command);
  // wait some time
  delay(1000);
  
}
