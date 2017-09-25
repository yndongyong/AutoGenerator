# AutoGenerator
Android apt library 生成findViewById ,绑定onClick，从getIntent中绑定传递的参数等的代码。

实例代码
    

    @BindView(R.id.tv_button1)
    TextView tv_1;

    @BindView(R.id.tv_button2)
    TextView tv_2;

    @Arg("KEY_EXTRA_A")
    String extra_name;

    @Arg("KEY_EXTRA_B")
    String extra_nick_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AutoGenerator.inject(this);
        Log.d("MainActivity", "tv_1 :" + tv_1.getText());
        Log.d("MainActivity", "tv_2 :" + tv_2.getText());

    }

    @OnClick({R.id.tv_button1, R.id.tv_button2})
    public void onBtnClick(View view) {
        if (view.getId() == R.id.tv_button1) {
            Toast.makeText(this, "tv_1.getText():" + tv_1.getText(), Toast.LENGTH_SHORT).show();
        } else {
            doInUiThread();
        }
    }
