import 'dart:math';

import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:expense_tracker/pages/splash_page.dart';
import 'package:expense_tracker/stores/login_store.dart';
import 'package:expense_tracker/widgets/new_transaction.dart';
import 'package:firebase_admob/firebase_admob.dart';
import 'package:provider/provider.dart';
import 'package:rate_my_app/rate_my_app.dart';
import 'package:url_launcher/url_launcher.dart';

import './models/transaction.dart';
import './widgets/transactions_list.dart';
import 'package:flutter/material.dart';
import './widgets/chart.dart';

const String testDevice = 'MobileId';

void main() => runApp(App());

class MyApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      // title: 'Expense Tracker',
      // theme: ThemeData(
      //   primarySwatch: Colors.purple,
      //   accentColor: Colors.amber,
      //   errorColor: Colors.red,
      //   fontFamily: 'Quicksand',
      //   textTheme: ThemeData.light().textTheme.copyWith(
      //         headline6: TextStyle(
      //           fontFamily: 'OpenSans',
      //           fontWeight: FontWeight.bold,
      //           fontSize: 20,
      //         ),
      //         button: TextStyle(
      //           color: Colors.white,
      //         ),
      //       ),
      //   appBarTheme: AppBarTheme(
      //     textTheme: ThemeData.light().textTheme.copyWith(
      //           headline6: TextStyle(
      //             fontFamily: 'OpenSans',
      //             fontWeight: FontWeight.bold,
      //             fontSize: 20,
      //           ),
      //           button: TextStyle(
      //             color: Colors.white,
      //           ),
      //         ),
      //   ),
      // ),
      home: SplashPage(),
    );
  }
}

class App extends StatefulWidget {
  @override
  _AppState createState() => _AppState();
}

class _AppState extends State<App> {
  @override
  Widget build(BuildContext context) {
    return MultiProvider(
      providers: [
        Provider<LoginStore>(
          create: (_) => LoginStore(),
        )
      ],
      child: const MaterialApp(
        home: SplashPage(),
      ),
    );
  }
}

class MyHomePage extends StatefulWidget {
  const MyHomePage({Key key}) : super(key: key);
  @override
  _MyHomePageState createState() => _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  final firestoreInstance = Firestore.instance;
  final List<TransactionModel> _userTransaction = [];

  List<TransactionModel> get _recentTransactions {
    return _userTransaction.where((tx) {
      return tx.date.isAfter(DateTime.now().subtract(
        Duration(days: 7),
      ));
    }).toList();
  }

  static const MobileAdTargetingInfo targetingInfo = MobileAdTargetingInfo(
    testDevices: testDevice != null ? <String>[testDevice] : null,
    nonPersonalizedAds: true,
    keywords: <String>['Game', 'Mario'],
  );

  BannerAd _bannerAd;
  InterstitialAd _interstitialAd;

  BannerAd createBannerAd() {
    return BannerAd(
        adUnitId: "ca-app-pub-4924437433046489/8477446233",
        size: AdSize.banner,
        targetingInfo: targetingInfo,
        listener: (MobileAdEvent event) {
          if (event == MobileAdEvent.loaded) {
            _bannerAd.show();
          }
        });
  }

  InterstitialAd createInterstitialAd() {
    return InterstitialAd(
        adUnitId: "ca-app-pub-4924437433046489/9181520795",
        targetingInfo: targetingInfo,
        listener: (MobileAdEvent event) {
          if (event == MobileAdEvent.failedToLoad) {
            _interstitialAd..load();
          } else if (event == MobileAdEvent.closed) {
            _interstitialAd = createInterstitialAd()..load();
          }
        });
  }

  void showInterstitialAd() {
    _interstitialAd..show();
  }

  void showRandomInterstitialAd() {
    Random r = new Random();
    bool value = r.nextBool();
    if (value == true) {
      _interstitialAd..show();
    }
  }

  @override
  void initState() {
    Firestore.instance.settings(persistenceEnabled: true);

    FirebaseAdMob.instance
        .initialize(appId: "ca-app-pub-4924437433046489/8477446233");
    _bannerAd = createBannerAd()
      ..load()
      ..show();
    FirebaseAdMob.instance
        .initialize(appId: "ca-app-pub-4924437433046489/9181520795");
    _interstitialAd = createInterstitialAd()
      ..load()
      ..show();
    super.initState();
  }

  @override
  void dispose() {
    _bannerAd.dispose();
    _interstitialAd.dispose();
    super.dispose();
  }

  RateMyApp _rateMyApp = RateMyApp(
      preferencesPrefix: 'rateMyApp_pro',
      minDays: 1,
      minLaunches: 2,
      remindDays: 2,
      remindLaunches: 4);

  void _addNewTransaction(
      String txTitle, double txAmount, DateTime chosenDate) {
    final newTx = TransactionModel(
      title: txTitle,
      amount: txAmount,
      date: chosenDate,
      id: DateTime.now().toString(),
    );
    setState(() {
      _userTransaction.add(newTx);
    });
  }

  void _deleteTransaction(String id) {
    setState(() {
      _userTransaction.removeWhere((tx) {
        return tx.id == id;
      });
    });
  }

  void _startModalBottomSheet(BuildContext ctx) {
    showModalBottomSheet(
        context: ctx,
        builder: (_) {
          return GestureDetector(
            onTap: () {},
            child: NewTransaction(_addNewTransaction),
            behavior: HitTestBehavior.opaque,
          );
        });
  }

  @override
  Widget build(BuildContext context) {
    createBannerAd();
    createInterstitialAd();
    final appBar = AppBar(
      title: Text('Expense Tracker'),
      actions: <Widget>[
        IconButton(
          icon: Icon(Icons.add, color: Colors.white, size: 30),
          onPressed: () {
            createInterstitialAd();
            _startModalBottomSheet(context);
          },
        ),
      ],
    );
    _launchURL() async {
      const url = 'https://appgallery5.huawei.com/#/app/C102647899';
      if (await canLaunch(url)) {
        await launch(url);
      } else {
        throw 'Cannot launch gallery store';
      }
    }

    _rateMyApp.init().then((_) {
      if (_rateMyApp.shouldOpenDialog) {
        //checking if the user has rated the app
        _rateMyApp.showStarRateDialog(
          context,
          title: 'What do you think about our App?',
          message: 'Please leave a rating',
          actionsBuilder: (_, stars) {
            return [
              FlatButton(
                child: Text('OK'),
                onPressed: () async {
                  print("Thanks for the " +
                      (stars == null ? '0' : stars.round().toString()) +
                      'star(s) !');
                  if (stars != null && (stars == 4 || stars == 5)) {
                    _launchURL();
                  } else {
                    //Redirect to feedback page to tell us how to make the app better
                  }
                  //Handles the result as you want.
                  await _rateMyApp
                      .callEvent(RateMyAppEventType.rateButtonPressed);
                  Navigator.pop<RateMyAppDialogButton>(
                      context, RateMyAppDialogButton.rate);
                },
              ),
            ];
          },
          dialogStyle: DialogStyle(
            titleAlign: TextAlign.center,
            messageAlign: TextAlign.center,
            messagePadding: EdgeInsets.only(bottom: 20.0),
          ),
          starRatingOptions: StarRatingOptions(),
          onDismissed: () =>
              _rateMyApp.callEvent(RateMyAppEventType.laterButtonPressed),
        );
      }
    });

    return Consumer<LoginStore>(builder: (_, loginStore, __) {
      return Scaffold(
        appBar: appBar,
        body: SingleChildScrollView(
          child: Column(
            //mainAxisAlignment: MainAxisAlignment.start,
            crossAxisAlignment: CrossAxisAlignment.center,
            children: <Widget>[
              Container(
                  height: (MediaQuery.of(context).size.height -
                          appBar.preferredSize.height -
                          MediaQuery.of(context).padding.top) *
                      0.3,
                  child: Chart(_recentTransactions)),
              Container(
                  height: (MediaQuery.of(context).size.height -
                          appBar.preferredSize.height -
                          MediaQuery.of(context).padding.top) *
                      0.7,
                  child: Transactionist(_userTransaction, _deleteTransaction))
            ],
          ),
        ),
        floatingActionButtonLocation: FloatingActionButtonLocation.centerFloat,
        floatingActionButton: Padding(
          padding: EdgeInsets.only(bottom: 25),
          child: FloatingActionButton(
            child: Icon(Icons.add),
            onPressed: () {
              _startModalBottomSheet(context);
              createInterstitialAd();
            },
          ),
        ),
      );
    });
  }
}
