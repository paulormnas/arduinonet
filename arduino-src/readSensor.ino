void setup() {
  // put your setup code here, to run once:
  Serial.begin(115200);

}

void loop() {
  // put your main code here, to run repeatedly:
  float tensao = analogRead(A0) * 0.00488;
  float temperatura = analogRead(A0) * 0.48828125;
  float luminosidade = analogRead(A0) * 0.00488;

  
  if(Serial.available()){
    byte incomingByte = Serial.read();
     switch(incomingByte){
      case 'v': 
        Serial.println(tensao);
        break;
      case 'e': 
        Serial.println(temperatura);
        break;
      case 'l': 
        Serial.println(luminosidade);
        break;
      default:
        break;
     }
  }
}
