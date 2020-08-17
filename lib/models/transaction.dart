import 'package:flutter/material.dart';

class TransactionModel {
  String id;
  String title;
  double amount;
  DateTime date;

  TransactionModel(
      {@required this.id,
      @required this.title,
      @required this.amount,
      @required this.date});

  String get transactId {
    return transactId;
  }

  void set transactId(String id) {
    this.transactId = id;
  }

  String get transactTitle {
    return transactId;
  }

  void set transactTitle(String title) {
    this.transactId = title;
  }

  double get transactAmt {
    return transactAmt;
  }

  void set transactAmt(double amount) {
    this.transactAmt = amount;
  }

  DateTime get transactDate {
    return transactDate;
  }

  void set transactDate(DateTime date) {
    this.transactDate = date;
  }
}
