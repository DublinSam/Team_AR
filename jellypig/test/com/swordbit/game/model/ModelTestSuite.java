package com.swordbit.game.model;

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;

import com.swordbit.game.model.eater.EaterTest;

@RunWith(value=org.junit.runners.Suite.class)
@SuiteClasses(value={BackgroundTest.class, EaterTest.class})

public class ModelTestSuite {
}
