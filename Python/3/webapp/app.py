from flask import Flask, render_template, request, redirect, url_for, abort
from flask_sqlalchemy import SQLAlchemy
from sqlalchemy import select

from model import ModelManager

app = Flask(__name__)
app.config.from_object("config.Config")
db = SQLAlchemy(app)

model_manager = ModelManager()


# Define the DataPoint model
class DataPoint(db.Model):
    id = db.mapped_column(db.Integer, primary_key=True)
    feature1 = db.mapped_column(db.Float, nullable=False)
    feature2 = db.mapped_column(db.Float, nullable=False)
    category = db.mapped_column(db.Integer, nullable=False)

    def to_dict(self):
        return {"id": self.id, "feature1": self.feature1,
                "feature2": self.feature2, "category": self.category}


def retrain_model_on_change():
    data_points = db.session.execute(select(DataPoint)).scalars().all()
    model_manager.train_model(data_points)


def add_data_point(feature1, feature2, category):
    new_data_point = DataPoint(feature1=feature1, feature2=feature2,
                               category=category)
    db.session.add(new_data_point)
    db.session.commit()

    retrain_model_on_change()

    return new_data_point.id


def delete_data_point(data_point):
    db.session.delete(data_point)
    db.session.commit()
    retrain_model_on_change()


with app.app_context():
    db.create_all()
    data_points = db.session.execute(select(DataPoint)).scalars().all()
    if data_points:
        model_manager.train_model(data_points)
    else:
        print("No data found")


@app.route("/")
def home():
    data_points = db.session.execute(select(DataPoint)).scalars().all()
    return render_template("home.html", data_points=data_points)


@app.route("/add", methods=["GET", "POST"])
def add():
    if request.method == "POST":
        try:
            feature1 = float(request.form["feature1"])
            feature2 = float(request.form["feature2"])
            category = int(request.form["category"])

            add_data_point(feature1, feature2, category)
            return redirect(url_for("home"))

        except (ValueError, TypeError):
            abort(400)

    return render_template("add.html")


@app.route('/delete/<record_id>', methods=['POST'])
def delete(record_id):
    data_point = db.session.execute(
        select(DataPoint).where(DataPoint.id == record_id)).scalars().first()
    if data_point:
        delete_data_point(data_point)
        return redirect(url_for('home'))
    else:
        abort(404)


@app.route("/predict", methods=["GET", "POST"])
def predict():
    if request.method == "POST":
        try:
            feature1 = float(request.form["feature1"])
            feature2 = float(request.form["feature2"])
            category = model_manager.predict_category([feature1, feature2])
            return render_template("predict_result.html", category=category)
        except ValueError:
            abort(400)

    return render_template("predict.html")


# API Endpoints
@app.route("/api/data", methods=["GET"])
def api_get_data():
    data_points = db.session.execute(select(DataPoint)).scalars().all()
    return [data_point.to_dict() for data_point in data_points]


@app.route("/api/data", methods=["POST"])
def api_add_data():
    data = request.json
    try:
        feature1 = float(data["feature1"])
        feature2 = float(data["feature2"])
        category = int(data["category"])
        new_data_point_id = add_data_point(feature1, feature2, category)
        return {"id": new_data_point_id}
    except (KeyError, ValueError):
        return {"error": "Invalid data"}, 400


@app.route("/api/data/<record_id>", methods=["DELETE"])
def api_delete_data(record_id):
    data_point = db.session.execute(
        select(DataPoint).where(DataPoint.id == record_id)).scalars().first()
    if not data_point:
        return {"error": "Record not found"}, 404
    delete_data_point(data_point)
    return {"id": record_id}


@app.route("/api/predictions", methods=["GET"])
def api_predict():
    feature1 = request.args.get("feature1")
    feature2 = request.args.get("feature2")

    if not feature1 or not feature2:
        return {"error": "Missing feature1 or feature2"}, 400

    try:
        feature1 = float(feature1)
        feature2 = float(feature2)
        category = model_manager.predict_category([feature1, feature2])

        category = int(category)

        return {"category": category}
    except (TypeError, ValueError):
        return {"error": "Invalid data"}, 400


@app.errorhandler(404)
def not_found_error(error):
    message = "The requested resource was not found."
    return render_template("error.html", error_code=error.code,
                           message=message), error.code


@app.errorhandler(400)
def bad_request_error(error):
    message = "Bad Request: The server could not understand the request due to invalid syntax."
    return render_template("error.html", error_code=error.code,
                           message=message), error.code


if __name__ == "__main__":
    app.run(debug=True)
