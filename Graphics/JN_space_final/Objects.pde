//Autor: Julia Nocuń, 247744

class Objects {

  PShape shape;
  float distance;
  float radius;
  float speed;
  float angle;
  boolean light, hue;
  PVector vector;
  color color1, color2;
  int moons;

  Objects[] moon;

  //materiały dla obiektów
  color ambientColor, specularColor, emissiveColor;
  float shininessValue;

  //światło
  void spot() {
    light = true;
  }

  Objects (PShape sh, int sc)
  {
    vector = PVector.random3D();
    radius = 10;
    distance = 0;
    speed = 0;
    moons = 0;
    color2 = color(255, 255, 255);
    vector.mult(distance);
    angle = 0;
    hue = false;
    shape = sh;
    shape.scale(sc);
  }

  //konstruktor dla obiektu o kształcie sfery lub prostopadłościanu
  Objects (float ra, float di, float sp, boolean sphere)
  {
    vector = PVector.random3D();
    radius = ra * 10;
    distance = di * 100;
    speed = sp / 30;
    color1 = color((int) random(255), (int) random(255), (int) random(255));
    color2 = color((int) random(32), (int) random(32), (int) random(32));
    //color1 = color(0, 0, 255);
    //color2 = color(0, 0, 255);
    
    moons = (int) random(1, 3);
    vector.mult(distance);
    angle = random(TWO_PI);

    hue = true;

    if (sphere)
      shape = createShape(SPHERE, radius);
    else
      shape = createShape(BOX, ra * 20, ra * 10, ra * 5);

    hue = true;
    spawnMoons(moons);
  }

  //konstruktor dla obiektu z teksturą wczytana z pliku
  Objects (float ra, float di, float sp, PImage img)
  {
    vector = PVector.random3D();

    radius = ra * 10;
    distance = di * 100;
    speed = sp / 30;
    color1 = color((int) random(255), (int) random(255), (int) random(255));
    color2 = color((int) random(32), (int) random(32), (int) random(32));
    moons = (int) random(1, 3);
    vector.mult(distance);
    angle = random(TWO_PI);

    shape = createShape(SPHERE, radius);
    shape.setTexture(img);

    hue = true;
    spawnMoons(moons);
  }

  //konstruktor dla obiektu z określonym kształtem PShape i skalą
  Objects (float ra, float di, float sp, PShape sh, int sc)
  {
    vector = PVector.random3D();
    radius = ra * 10;
    distance = di * 100;
    speed = sp / 30;
    color1 = color((int) random(255), (int) random(255), (int) random(255));
    color2 = color((int) random(32), (int) random(32), (int) random(32));
    //color1 = color(0, 0, 255);
    //color2 = color(0, 0, 255);
    moons = (int) random(1, 3);
    vector.mult(distance);
    angle = random(TWO_PI);
    hue = false;
    shape = sh;
    shape.scale(sc);
    spawnMoons(moons);
  }

  //konstruktor dla obiektu bez tekstury wczytanej z pliku
  Objects (float ra, float di, float sp)
  {
    vector = PVector.random3D();
    radius = ra;
    distance = di;
    speed = sp / 30;
    color1 = color((int) random(255), (int) random(255), (int) random(255));
    color2 = color((int) random(32), (int) random(32), (int) random(32));
    //color1 = color(0, 0, 255);
    //color2 = color(0, 0, 255);
    moons = 0;
    vector.mult(distance);
    angle = random(TWO_PI);
    hue = true;
    shape = createShape(SPHERE, radius);

    //generowanie losowych wartości dla materiałów
    ambientColor = color(0, 0, 0);
    specularColor = color(0, 0, 255);
    emissiveColor = color((int) random(255), (int) random(255), (int) random(255));
    emissiveColor = color(0,0,255);
    shininessValue = (10);
  }

  //generowanie księżyców dla planety
  void spawnMoons(int number)
  {
    moon = new Objects[number];

    for (int i = 0; i < moon.length; i++)
    {
      float r = radius / 4;
      float d = random((radius + r), (radius + r) * 2);
      float s = random(-2, 2);

      moon[i] = new Objects(r, d, s);
    }
  }

  //wyświetlanie obiektów
  void show()
  {
    pushMatrix();
    {
      noStroke();
      PVector tmp = new PVector(1, 0, 1);
      PVector perVec = vector.cross(tmp);
      rotate(angle, perVec.x, perVec.y, perVec.z);

      if (light)
        spotLight(255, 255, 0, vector.x - (radius / 2), vector.y - (radius / 2), vector.z - (radius / 2), 1, 0, 1, PI / 6, 1);

      translate(vector.x, vector.y, vector.z);

      // Ustawienia materiałów
      ambient(ambientColor);
      specular(specularColor);
      shininess(shininessValue);
      emissive(emissiveColor);

      shape(shape);

      if (hue)
        shape.setFill(color1);

      shape.setEmissive(color2);

      angle = angle + speed;

      if (moon != null)
      {
        for (int i = 0; i < moon.length; i++)
          moon[i].show();
      }
    }
    popMatrix();
  }
}
