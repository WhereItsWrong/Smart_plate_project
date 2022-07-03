#include <SoftwareSerial.h>
#include <stdlib.h>
#include <math.h>
#include "HX711.h"

#define BT_RXD 8
#define BT_TXD 7
SoftwareSerial bluetooth(7, 8);

#define calibration_factor -7050.0
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
  int num1 = (int) scale.get_units(); //무게정보 받기
  int num2 = num1;                    //무게정보를 받은 정수에 바로 연산을 처리하면 멈추는것을 확인(이유 확인 불가)
  
  if (num2 < 0)                      //무게정보 양수 변환
  {                   
    num2 = -num2;
  }
  
                                    
  if(num2 != 0){                      //자리수 구하기
    count = number_of_digits(num2);
  }
  else if (num2 == 0)
  {
    count = 1;
  }
  
  
  //커맨드 스트링 생성 과정
  char command[] = "AT+IBE0";
  
  char str1[10] = "";
  char str2[10] = "";
  
  itoa(num2, str2,10);
  
  char zero[2] = "0";
  
  int i = 0;

  //0 추가 과정
  
  while(i<8-count){
    strcat(str1, zero);
    i = i + 1;
    
  }
  
  strcat(str1, str2);
  strcat(command, str1);
  
  bluetooth.write(command);
  Serial.println(command);
  // wait some time
  delay(1000);
  
}
