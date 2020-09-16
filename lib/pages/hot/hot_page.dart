import 'package:flutter/material.dart';
import 'package:movie_app/provider/hot_page_model.dart';
import 'package:movie_app/provider/provider_widget.dart';
import 'package:movie_app/widget/loading_widget.dart';
import 'package:provider/provider.dart';

/// 热门
class HotPage extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => HotPageState();
}

class HotPageState extends State<HotPage> with AutomaticKeepAliveClientMixin {
  @override
  Widget build(BuildContext context) {
    super.build(context);
    return ProviderWidget<HotPageModel>(
      model: HotPageModel(),
      onModelInitial: (model) {
        model.init();
      },
      builder: (context, model, child) {
        return DefaultTabController(
          length: model.tabItems.length,
          child: Scaffold(
            appBar: AppBar(
              title: Text('热门'),
              centerTitle: true,
              elevation: 0,
            ),
            body: HotPageWidget(),
          ),
        );
      },
    );
  }

  @override
  bool get wantKeepAlive => true;
}

class HotPageWidget extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    HotPageModel model = Provider.of(context);
    if (model.isInit) {
      return LoadingWidget();
    }
    return Column(
      children: <Widget>[
        Material(
          color: Colors.white,
          child: SizedBox(
            width: double.infinity,
            height: 48,
            child: TabBar(
              tabs: model.tabItems
                  .map((tabItem) => Tab(text: tabItem.name))
                  .toList(),
              indicatorColor: Colors.black87,
              indicatorSize: TabBarIndicatorSize.label,
            ),
          ),
        ),
        Expanded(
          child: TabBarView(
            children: model.tabPages,
          ),
        )
      ],
    );
  }
}
