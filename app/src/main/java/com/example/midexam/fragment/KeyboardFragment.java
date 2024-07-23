package com.example.midexam.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.midexam.R;

public class KeyboardFragment extends Fragment {

    public static View view;
    //各个模式的值
    public static final int MODE_OVER = 0;
    public static final int MODE_SUM = 1;
    public static final int MODE_INVEST = 2;
    public static final int MODE_SURPLUS = 3;
    public static final int MODE_CONSUME = 4;

    //键盘上按键的对应值，相当于id
    private final int ZERO = 0;
    private final int ONE = 1;
    private final int TWO = 2;
    private final int THREE = 3;
    private final int FOUR = 4;
    private final int FIVE = 5;
    private final int SIX = 6;
    private final int SEVEN = 7;
    private final int EIGHT = 8;
    private final int NINE = 9;
    private final int DOT = 10;
    private final int BACK = 11;
    private final int SUCCESS = 12;

    private int positive = 0;//整数的位数
    private int negative = 0;//小数的位数，但是1表示“正在进行小数输入”，2和3才是表示一位小数和两位小数
    private int inputIntegers = 0;//已经输入的整数
    private int inputDecimals = 0;//已经输入的小数
    public static int MODE = 0;//当前的模式
    private static ConstraintLayout keyboard;
    private View background;

    //以下就是一些组件而已
    private TextView one;
    private TextView two;
    private TextView three;
    private TextView four;
    private TextView five;
    private TextView six;
    private TextView seven;
    private TextView eight;
    private TextView nine;
    private TextView zero;
    private TextView dot;
    private TextView modeText;
    private TextView changeValue;
    private TextView success;
    private ConstraintLayout back;
    private ConstraintLayout recordWindow;
    private TextView expenditureButton;
    private TextView incomeButton;
    private EditText tips;

    //记账用的，表示“支出”或“收入”
    private int EXorIN = 0;
    private final int EX = 1;
    private final int IN = -1;

    //drawable资源
    private Drawable buttonStroke;
    private Drawable buttonSolid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /*view = inflater.inflate(R.layout.fragment_edit, container, false);


        initView();
        initListener();
        initKeyboard(MODE_OVER);
        */

        return view;
    }

    /*
    private void initListener() {
        background.setOnClickListener(v -> hideKeyboard());

        zero.setOnClickListener(v -> fromKeyboardGetValue(ZERO));
        one.setOnClickListener(v -> fromKeyboardGetValue(ONE));
        two.setOnClickListener(v -> fromKeyboardGetValue(TWO));
        three.setOnClickListener(v -> fromKeyboardGetValue(THREE));
        four.setOnClickListener(v -> fromKeyboardGetValue(FOUR));
        five.setOnClickListener(v -> fromKeyboardGetValue(FIVE));
        six.setOnClickListener(v -> fromKeyboardGetValue(SIX));
        seven.setOnClickListener(v -> fromKeyboardGetValue(SEVEN));
        eight.setOnClickListener(v -> fromKeyboardGetValue(EIGHT));
        nine.setOnClickListener(v -> fromKeyboardGetValue(NINE));
        dot.setOnClickListener(v -> fromKeyboardGetValue(DOT));
        back.setOnClickListener(v -> fromKeyboardGetValue(BACK));
        success.setOnClickListener(v -> fromKeyboardGetValue(SUCCESS));

        expenditureButton.setOnClickListener(v -> {
            expenditureButton.setBackground(buttonStroke);
            incomeButton.setBackground(buttonSolid);
            EXorIN = EX;
        });
        incomeButton.setOnClickListener(v -> {
            expenditureButton.setBackground(buttonSolid);
            incomeButton.setBackground(buttonStroke);
            EXorIN = IN;
        });
    }

    public void initView() {
        buttonStroke = ContextCompat.getDrawable(getActivity(), R.drawable.shape_button_black_stroke);
        buttonSolid = ContextCompat.getDrawable(getActivity(), R.drawable.shape_button);

        background = view.findViewById(R.id.keyboard_background);

        keyboard = view.findViewById(R.id.keyboard);
        one = view.findViewById(R.id.one);
        two = view.findViewById(R.id.two);
        three = view.findViewById(R.id.three);
        four = view.findViewById(R.id.four);
        five = view.findViewById(R.id.five);
        six = view.findViewById(R.id.six);
        seven = view.findViewById(R.id.seven);
        eight = view.findViewById(R.id.eight);
        nine = view.findViewById(R.id.nine);
        zero = view.findViewById(R.id.zero);
        dot = view.findViewById(R.id.dot);
        modeText = view.findViewById(R.id.change_mode);
        changeValue = view.findViewById(R.id.change_value);
        success = view.findViewById(R.id.success);
        back = view.findViewById(R.id.back);

        recordWindow = view.findViewById(R.id.record_window);
        expenditureButton = view.findViewById(R.id.expenditure);
        incomeButton = view.findViewById(R.id.income);
        tips = view.findViewById(R.id.tips);
    }

    //每次呼出键盘或切换模式，当然得重新初始化一遍
    public void initKeyboard(int mode) {
        MODE = mode;
        EXorIN = 1;
        expenditureButton.setBackground(buttonStroke);
        incomeButton.setBackground(buttonSolid);
        tips.setText(null);
        recordWindow.setVisibility(View.INVISIBLE);
        inputIntegers = 0;
        inputDecimals = 0;
        positive = 0;
        negative = 0;

        if (mode == MODE_OVER) return;


        //因为fragment是动态加载到DesktopActivity和PlanActivity
        //键盘的隐藏和调用由这两个activity调用
        //所以在这里会去调用所在activity的callKeyboard函数
        if (mode == MODE_SUM) {
            modeText.setText("余额");
            PlanActivity.callKeyboard(MODE_SUM);
        } else if (mode == MODE_INVEST) {
            modeText.setText("存储 投资");
            PlanActivity.callKeyboard(MODE_INVEST);
        } else if (mode == MODE_SURPLUS) {
            modeText.setText("每日上限");
            PlanActivity.callKeyboard(MODE_SURPLUS);
        } else if (mode == MODE_CONSUME) {
            modeText.setText("今日");
            recordWindow.setVisibility(View.VISIBLE);
            DesktopActivity.callKeyboard(MODE_CONSUME);
        }
        changeValue.setText("0");
    }

    //键入函数，感觉可以优化，懒
    private void fromKeyboardGetValue(int key) {
        if (key == SUCCESS) {
            updateUserDate();
            hideKeyboard();
            initKeyboard(MODE_OVER);
            return;
        } else if (negative > 1 && key == BACK) {
            negative--;
            inputDecimals /= 10;
            changeValue.setText(String.valueOf(inputIntegers));
            changeValue.append(".");
            changeValue.append(String.valueOf(inputDecimals));
            return;
        } else if (negative == 1 && key == BACK) {
            negative--;
            inputDecimals /= 10;
            changeValue.setText(String.valueOf(inputIntegers));
            return;
        } else if (negative == 0 && positive != 0 && key == BACK) {
            positive--;
            inputIntegers /= 10;
            changeValue.setText(String.valueOf(inputIntegers));
            return;
        } else if (negative == 0 && positive == 0 && key == BACK) return;

        if (key != DOT && (positive == 7 || negative == 3)) {
            Toast.makeText(getActivity(), "到达限制啦", Toast.LENGTH_SHORT).show();
            return;
        }

        if (negative == 0) {
            if (positive == 0 && key == DOT) {
                negative = 1;
                changeValue.setText("0.");
            } else if (positive == 0 && key != ZERO) {
                positive = 1;
                inputIntegers = key;
                changeValue.setText(String.valueOf(inputIntegers));
            } else if (positive > 0 && key == DOT) {
                negative = 1;
                changeValue.setText(String.valueOf(inputIntegers));
                changeValue.append(".");
            } else if (positive > 0) {
                positive++;
                inputIntegers *= 10;
                inputIntegers += key;
                changeValue.setText(String.valueOf(inputIntegers));
            }
        } else if (key != DOT) {
            negative++;
            inputDecimals *= 10;
            inputDecimals += key;
            changeValue.setText(String.valueOf(inputIntegers));
            changeValue.append(".");
            changeValue.append(String.valueOf(inputDecimals));
        }
    }

    //同理，调用所在activity的callKeyboard来隐藏
    private void hideKeyboard() {
        if (MODE == MODE_SUM || MODE == MODE_INVEST || MODE == MODE_SURPLUS) {
            PlanActivity.callKeyboard(MODE_OVER);
        } else if (MODE == MODE_CONSUME) {
            DesktopActivity.callKeyboard(MODE_OVER);
        }
    }

    private void updateUserDate() {
        float value;
        if (negative < 3)
            value = (float) ((float) EXorIN * ((float) inputIntegers + 0.1 * inputDecimals));
        else value = (float) ((float) EXorIN * ((float) inputIntegers + 0.01 * inputDecimals));

        if (MODE == MODE_SUM) {
            UserData.changeSum(getActivity(), value);
            PlanActivity.sumValue.setText(UserData.sumString);
        } else if (MODE == MODE_INVEST) {
            UserData.changeInvest(getActivity(), value);
            PlanActivity.investValue.setText(UserData.investString);
        } else if (MODE == MODE_SURPLUS) {
            UserData.changeConsume(getActivity(), 0F);
            UserData.changeSurplus(getActivity(), value);
            PlanActivity.surplusValue.setText(UserData.surplusString);
        } else if (MODE == MODE_CONSUME) {
            UserData.changeConsume(getActivity(), value + UserData.consume);
            HomeFragment.initValue();
        }
    }
    */
}