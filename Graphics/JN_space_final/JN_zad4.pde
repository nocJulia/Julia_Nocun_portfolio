//Autor: Julia Nocuń, 247744

import peasy.*;
import peasy.org.apache.commons.math.*;
import peasy.org.apache.commons.math.geometry.*;
import processing.sound.*;
import processing.core.*;
import processing.opengl.*;

PGraphicsOpenGL canvas;
PGraphics buffer;
SoundFile file;

boolean xPlus, xMinus, yPlus, yMinus, zPlus, zMinus;
PeasyCam cam;

float time = 0.01f;
float X_posit = 0;
float Y_posit = 0;
float Z_posit = 0;

float speed = 1;

Objects sun;
Objects[] planet = new Objects[12];
PShape ufo;

// Lista meteorytów
ArrayList<Meteor> meteors = new ArrayList<Meteor>();

boolean viewFromShip = false; // Flaga do trybu widoku

void setup() {

  //ustawienie kamery z odległością początkową 1300
  cam = new PeasyCam(this, 1300);

  //ustawienie trybu pełnoekranowego
  fullScreen(P3D);
  noStroke();

  file = new SoundFile(this, "HS_satellite.mp3");

  // Załadowanie obiektów 3D
  PShape sh = loadShape("Flower.obj");
  sh.scale(0.07);
  ufo = loadShape("ufo.obj");
  ufo.scale(5);

  // Załadowanie tekstur dla planet
  PImage im1 = loadImage("earth.jpg");
  PImage im2 = loadImage("luna.jpg");
  PImage im3 = loadImage("jupiter.jpg");
  PImage im4 = loadImage("pluto.jpg");
  PImage im5 = loadImage("saturn.jpg");
  PImage im6 = loadImage("sun.jpg");
  PImage im7 = loadImage("uranus.jpg");
  PImage im8 = loadImage("neptune.jpg");
  PImage im9 = loadImage("mercury.jpg");
  PImage im10 = loadImage("mars.jpg");
  PImage im11 = loadImage("venus.jpg");

  //Słońce
  sun = new Objects(12, 0, 1, im6);

  //inicjalizacja planet z odpowiednimi parametrami
  // Promien, odleglosc, predkosc
  planet[0] = new Objects(3.5, 5, 0.6, false);
  planet[1] = new Objects(5, 4, 1, im1);
  planet[2] = new Objects(4, 6, 0.7, im4);
  planet[0].spot();
  planet[3] = new Objects(5, 8, 0.5, sh, -30);
  planet[4] = new Objects(7, 10, 0.3, im2);
  planet[5] = new Objects(9, 12, 0.4, im3);
  planet[6] = new Objects(10, 12.5, 0.2, im5);
  planet[7] = new Objects(3, 14, 0.3, im7);
  planet[8] = new Objects(6, 16, 0.6, im8);
  planet[9] = new Objects(4, 18, 0.9, im9);
  planet[10] = new Objects(8, 5, 0.5, im10);
  planet[11] = new Objects(6, 20, 0.2, im11);

  // Odtworzenie pliku dźwiękowego
  file.play();
}

void draw() {
  background(#010111);
  sun.show();
  ambientLight(0, 0, 0, 0, 0, 0);
  lightSpecular(255, 255, 255);
  pointLight(255, 255, 255, 0, 0, 0);

  // Wyświetlenie planet
  for (int i = 0; i < 12; ++i)
    planet[i].show();

  //ustawienia oświetlenia

  directionalLight(204, 204, 204, 0, 0, -1);
  specular(255, 255, 255);
  sphere(30);
  translate(60, 0, 0);
  specular(204, 102, 0);
  sphere(30);

  // Aktualizacja pozycji statku kosmicznego
  if (xMinus) X_posit -= speed;
  if (xPlus) X_posit += speed;
  if (yMinus) Y_posit -= speed;
  if (yPlus) Y_posit += speed;
  if (zMinus) Z_posit -= speed;
  if (zPlus) Z_posit += speed;

  // Obrócenie statku
  pushMatrix();
  translate(width / 2 + X_posit, height / 2 + Y_posit, Z_posit);
  rotateX(PI);

  // Ustawienie reflektora jako punktowego źródła światła
  spotLight(255, 204, 0, 0, 0, 0, 0, 1, 1, PI/2, 1);

  // Ustawienie materiału statku kosmicznego
  ambient(50, 50, 50);
  specular(0, 0, 255);
  shininess(30.0);
  emissive(50, 0, 0);
  shape(ufo);
  popMatrix();

  // Wyświetlanie i aktualizacja meteorytów
  for (int i = meteors.size() - 1; i >= 0; i--) {
    Meteor m = meteors.get(i);
    m.update();
    m.show();
    // Usunięcie meteorytu, jeśli jest poza ekranem
    if (m.offscreen()) {
      meteors.remove(i);
    }
  }

  // Planeta głowna
  pushMatrix();
  rotate(-3 * time * speed);
  translate(210, 0);
  fill(#009966);
  stroke(35);
  sphere(25);
  popMatrix();

  time += 0.005f;

  // Ustawienie kamery w zależności od trybu widoku
  if (viewFromShip) {
    cam.setActive(false);
    camera(X_posit, Y_posit, Z_posit + 200, X_posit, Y_posit, Z_posit, 0, 1, 0); // Kamera podążająca za statkiem
  } else {
    cam.setActive(true);
  }
}

// Przemieszczenie statku kosmicznego w głąb sceny
//po kliknięciu środkowym przyciskiem myszy
void mousePressed() {
  if (mouseButton == CENTER)
    Z_posit -= 20;
}

// Aktualizacja flag na podstawie wciśniętych klawiszy
void keyPressed() {
  if (key != CODED && keyCode == 'J' || key == CODED && keyCode == LEFT)
    xMinus = true;
  else if (key != CODED && keyCode == 'L' || key == CODED && keyCode == RIGHT)
    xPlus = true;
  else if (key != CODED && keyCode == 'I' || key == CODED && keyCode == UP)
    yMinus = true;
  else if (key != CODED && keyCode == 'K' || key == CODED && keyCode == DOWN)
    yPlus = true;
  else if (key != CODED && keyCode == 'N')
    zMinus = true;
  else if (key != CODED && keyCode == 'M')
    zPlus = true;
  else if (key != CODED && keyCode == 'Q')
    speed -= 0.6;
  else if (key != CODED && keyCode == 'W')
    speed += 0.6;
  else if (key != CODED && keyCode == 'A') {
    viewFromShip = false; // Ustawienie trybu widoku ogólnego
    cam.setActive(true);
    cam.lookAt(0, 0, 0, 1000);
  } else if (key != CODED && keyCode == 'S') {
    viewFromShip = true; // Ustawienie trybu widoku z perspektywy statku kosmicznego
  } else if (key == ' ') {
    meteors.add(new Meteor(width / 2 + X_posit, height / 2 + Y_posit - 20, Z_posit, new PVector(0, 10, 0))); // Wystrzelenie meteorytu po naciśnięciu spacji
  }
}

// Zaktualizowanie flag po zwolnieniu klawiszy
void keyReleased() {
  if (key != CODED && keyCode == 'J' || key == CODED && keyCode == LEFT)
    xMinus = false;
  else if (key != CODED && keyCode == 'L' || key == CODED && keyCode == RIGHT)
    xPlus = false;
  else if (key != CODED && keyCode == 'I' || key == CODED && keyCode == UP)
    yMinus = false;
  else if (key != CODED && keyCode == 'K' || key == CODED && keyCode == DOWN)
    yPlus = false;
  else if (key != CODED && keyCode == 'N')
    zMinus = false;
  else if (key != CODED && keyCode == 'M')
    zPlus = false;
}

// Klasa reprezentująca meteoryt
class Meteor {
  PVector pos; // Pozycja początkowa meteorytu
  PVector vel; // Kierunek meteorytu
  float size; // Rozmiar meteorytu

  Meteor(float x, float y, float z, PVector velocity) {
    pos = new PVector(x, y, z);
    vel = velocity; // Kierunek meteorytu
    size = random(5, 15); // Losowy rozmiar meteorytu
  }

  void update() {
    // Aktualizacja pozycji początkowej meteorytu
    pos.add(vel);
  }

  void show() {
    // Wyświetlanie meteorytu jako kuli
    pushMatrix();
    translate(pos.x, pos.y, pos.z);
    fill(150);
    sphere(size);
    popMatrix();
  }

  boolean offscreen() {
    // Sprawdzenie, czy meteoryt jest poza ekranem
    return pos.z < -1000 || pos.y > height;
  }
}
