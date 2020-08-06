import 'package:flutter/material.dart';
import 'package:flutter_mobx/flutter_mobx.dart';
import 'package:provider/provider.dart';
import '../models/transaction.dart';
import '../widgets/loader_hud.dart';
import '../stores/login_store.dart';
import 'package:flutter_mobx/flutter_mobx.dart';
import '../theme.dart';

class LoginPage extends StatefulWidget {
  const LoginPage({Key key}):super(key:key);
  @override
  _LoginPageState createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  TextEditingController phoneController = TextEditingController();

  @override
  Widget build(BuildContext context) {
    return Consumer<LoginStore>(
      builder: (_,loginStore,__){
        return Observer(
          builder: (_)=>LoaderHUD(
            inAsyncCall:loginStore.isLoginLoading,
            child: Scaffold(
              backgroundColor: Colors.white,
              key: loginStore.loginScaffoldKey,
              body: SafeArea(
                child: SingleChildScrollView(
                  child: Container(
                    height: MediaQuery.of(context).size.height,
                    child: Column(
                      children: <Widget>[
                        Expanded(
                          flex: 2,
                          child: Column(
                            children: <Widget>[
                              Container(
                                margin: const EdgeInsets.symmetric(horizontal: 20,vertical: 20),
                                child: Stack(
                                  children: <Widget>[
                                    Center(
                                      child: Container(
                                        height: 240,
                                        constraints: const BoxConstraints(
                                          maxWidth: 500,
                                        ),
                                        margin: const EdgeInsets.only(top: 100),
                                        decoration: const BoxDecoration(color: Color(0xFFE1E0F5),borderRadius: BorderRadius.all(Radius.circular(30))),
                                      ),
                                    ),
                                    Center(
                                      child: Container(
                                        constraints: const BoxConstraints(maxHeight: 340),
                                        margin: const EdgeInsets.symmetric(horizontal: 8),
                                        child: Image.asset('assets/images/login.png')),
                                    ),
                                  ],
                                ),
                              ),
                              Container(
                                margin: const EdgeInsets.symmetric(horizontal: 10),
                                child: Text('Expense Tracker',
                                style: TextStyle(
                                  color: MyColors.primaryColor,
                                  fontSize: 30,
                                  fontWeight: FontWeight.w800
                                ),),
                              ),
                            ],
                          ),
                        ),
                        Expanded(
                          flex: 1,
                          child: Column(
                            children: <Widget>[
                              Container(
                                constraints: const BoxConstraints(
                                  maxWidth: 500,
                                ),
                                margin: const EdgeInsets.symmetric(horizontal: 10),
                                child: RichText(
                                  textAlign: TextAlign.center,
                                  text: TextSpan(
                                    children: <TextSpan>[
                                      TextSpan(text:'We will send you an ',style:TextStyle(color: MyColors.primaryColor)),
                                      TextSpan(
                                        text: 'One Time Password ',style: TextStyle(color: MyColors.primaryColor,fontWeight: FontWeight.bold)),
                                      TextSpan(
                                        text: 'on this mobile number ',style: TextStyle(color: MyColors.primaryColor)),
                                    ]),
                                ),
                              ),
                              Container(

                              )
                            ],
                          ),
                        )
                      ],
                    ),
                  ),
                ),
              ),
            ),
          )
          )
      }
      ,);
  }
}
