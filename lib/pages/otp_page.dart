import 'package:expense_tracker/stores/login_store.dart';
import 'package:expense_tracker/theme.dart';
import 'package:expense_tracker/widgets/loader_hud.dart';
import 'package:flutter/material.dart';
import 'package:numeric_keyboard/numeric_keyboard.dart';
import 'package:provider/provider.dart';

class OtpPage extends StatefulWidget {
  const OtpPage({Key key}) : super(key: key);

  @override
  _OtpPageState createState() => _OtpPageState();
}

class _OtpPageState extends State<OtpPage> {
  String text = '';
  void _onKeyBoardTap(String value) {
    setState(() {
      text = text + value;
    });
  }

  Widget otpNumberWidget(int position) {
    try {
      return Container(
        height: 40,
        width: 40,
        decoration: BoxDecoration(
            border: Border.all(color: Colors.black, width: 0),
            borderRadius: const BorderRadius.all(Radius.circular(8))),
        child: Center(
          child: Text(
            text[position],
            style: TextStyle(color: Colors.black),
          ),
        ),
      );
    } catch (e) {
      return Container(
        height: 40,
        width: 40,
        decoration: BoxDecoration(
            border: Border.all(color: Colors.black, width: 0),
            borderRadius: const BorderRadius.all(Radius.circular(8))),
      );
    }
  }

  @override
  Widget build(BuildContext context) {
    return Consumer<LoginStore>(
      builder: (_, loginStrore, __) => LoaderHUD(
        inAsyncCall: loginStrore.isOtpLoading,
        child: Scaffold(
          backgroundColor: Colors.white,
          key: loginStrore.otpScaffoldKey,
          appBar: AppBar(
            leading: IconButton(
              icon: Container(
                padding: const EdgeInsets.all(10),
                decoration: const BoxDecoration(
                  borderRadius: const BorderRadius.all(Radius.circular(20)),
                  color: MyColors.primaryColorLight,
                ),
                child: Icon(
                  Icons.arrow_back_ios,
                  color: MyColors.primaryColor,
                  size: 16,
                ),
              ),
              onPressed: () => Navigator.of(context).pop(),
            ),
            elevation: 0,
            backgroundColor: Colors.white,
            brightness: Brightness.light,
          ),
          body: SafeArea(
            child: Column(
              mainAxisAlignment: MainAxisAlignment.spaceBetween,
              crossAxisAlignment: CrossAxisAlignment.center,
              children: <Widget>[
                Expanded(
                  child: Column(
                    mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                    children: <Widget>[
                      Container(
                          margin: const EdgeInsets.symmetric(horizontal: 20),
                          child: Text(
                              'Enter the 6-digit verification code sent to your phone',
                              style: TextStyle(
                                  color: Colors.black,
                                  fontSize: 26,
                                  fontWeight: FontWeight.w500))),
                      Container(
                        constraints: const BoxConstraints(
                          maxWidth: 500,
                        ),
                        child: Row(
                          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                          crossAxisAlignment: CrossAxisAlignment.center,
                          children: <Widget>[
                            otpNumberWidget(0),
                            otpNumberWidget(1),
                            otpNumberWidget(2),
                            otpNumberWidget(3),
                            otpNumberWidget(4),
                            otpNumberWidget(5),
                          ],
                        ),
                      ),
                    ],
                  ),
                ),
                Container(
                  margin:
                      const EdgeInsets.symmetric(horizontal: 20, vertical: 10),
                  constraints: const BoxConstraints(maxWidth: 500),
                  child: RaisedButton(
                    onPressed: () {
                      loginStrore.validateOtpAndLogin(context, text);
                    },
                    color: MyColors.primaryColor,
                    shape: const RoundedRectangleBorder(
                        borderRadius: BorderRadius.all(Radius.circular(14))),
                    child: Container(
                      padding: const EdgeInsets.symmetric(
                          vertical: 8, horizontal: 8),
                      child: Row(
                        mainAxisAlignment: MainAxisAlignment.spaceBetween,
                        children: <Widget>[
                          Text(
                            'Confirm',
                            style: TextStyle(color: Colors.white),
                          ),
                          Container(
                            padding: const EdgeInsets.all(8),
                            decoration: BoxDecoration(
                              borderRadius:
                                  const BorderRadius.all(Radius.circular(20)),
                              color: MyColors.primaryColorLight,
                            ),
                            child: Icon(
                              Icons.arrow_forward_ios,
                              color: Colors.white,
                              size: 16,
                            ),
                          ),
                        ],
                      ),
                    ),
                  ),
                ),
                NumericKeyboard(
                  onKeyboardTap: _onKeyBoardTap,
                  textColor: MyColors.primaryColorLight,
                  rightIcon: Icon(
                    Icons.backspace,
                    color: MyColors.primaryColorLight,
                  ),
                  rightButtonFn: () {
                    setState(() {
                      text = text.substring(0, text.length - 1);
                    });
                  },
                )
              ],
            ),
          ),
        ),
      ),
    );
  }
}
