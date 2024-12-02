/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.maven.enforcer.rules;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.model.Profile;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class MutuallyExclusiveProfilesSetTest {

    @Mock
    private Profile profileA;

    @Mock
    private Profile profileB;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testNoneSelectedNoneRequired() {
        String profileList = "a, b, c";
        MutuallyExclusiveProfilesSet testObject = new MutuallyExclusiveProfilesSet();
        testObject.setProfilesString(profileList);

        List<Profile> emptyList = new ArrayList<>();

        assertTrue(testObject.ruleIsSatisfied(emptyList), "Should be OK if no profile from the list is selected");
    }

    @Test
    public void testNoneSelectedOneRequired() {
        String profileList = "a, b, c";
        MutuallyExclusiveProfilesSet testObject = new MutuallyExclusiveProfilesSet();
        testObject.setProfilesString(profileList);
        testObject.setRequireOne(true);

        List<Profile> emptyList = new ArrayList<>();

        assertFalse(testObject.ruleIsSatisfied(emptyList), "Should FAIL if no profile from the list is selected");
    }

    @Test
    public void testOneSelectedNoneRequired() {
        String profileList = "a, b, c";
        MutuallyExclusiveProfilesSet testObject = new MutuallyExclusiveProfilesSet();
        testObject.setProfilesString(profileList);

        List<Profile> emptyList = new ArrayList<>();

        assertTrue(testObject.ruleIsSatisfied(emptyList), "Should be OK if ONE profile from the list is selected");
    }

    @Test
    public void testOneSelectedOneRequired() {
        String profileList = "a, b, c";
        MutuallyExclusiveProfilesSet testObject = new MutuallyExclusiveProfilesSet();
        testObject.setProfilesString(profileList);
        testObject.setRequireOne(true);

        List<Profile> emptyList = List.of(profileA);

        when(profileA.getId()).thenReturn("a");

        assertTrue(testObject.ruleIsSatisfied(emptyList), "Should be OK if ONE profile from the list is selected");
    }

    @Test
    public void testOtherOneSelectedOneRequired() {
        String profileList = "a, b, c";
        MutuallyExclusiveProfilesSet testObject = new MutuallyExclusiveProfilesSet();
        testObject.setProfilesString(profileList);
        testObject.setRequireOne(true);

        List<Profile> emptyList = List.of(profileA);

        when(profileA.getId()).thenReturn("d");

        assertFalse(testObject.ruleIsSatisfied(emptyList), "Should FAIL if No profile from the list is selected");
    }

    @Test
    public void testTwoSelectedOneRequired() {
        String profileList = "a, b, c";
        MutuallyExclusiveProfilesSet testObject = new MutuallyExclusiveProfilesSet();
        testObject.setProfilesString(profileList);
        testObject.setRequireOne(true);

        List<Profile> emptyList = List.of(profileA, profileB);

        when(profileA.getId()).thenReturn("a");
        when(profileB.getId()).thenReturn("b");

        assertFalse(testObject.ruleIsSatisfied(emptyList), "Should FAIL if TWO profiles from the list are selected");
    }

    @Test
    public void testStringificationNoneRequired() {
        String profileList = "a, b, c";
        MutuallyExclusiveProfilesSet testObject = new MutuallyExclusiveProfilesSet();
        testObject.setProfilesString(profileList);

        String expectedString = "MutuallyExclusiveProfilesSet[oneRequired=false, profileList=[a, b, c]]";
        String result = testObject.toString();
        assertEquals(expectedString, result, "Expected '" + expectedString + "'");
    }

    @Test
    public void testStringificationOneRequired() {
        String profileList = "a, x, y";
        MutuallyExclusiveProfilesSet testObject = new MutuallyExclusiveProfilesSet();
        testObject.setProfilesString(profileList);
        testObject.setRequireOne(true);

        String expectedString = "MutuallyExclusiveProfilesSet[oneRequired=true, profileList=[a, x, y]]";
        String result = testObject.toString();
        assertEquals(expectedString, result, "Expected '" + expectedString + "'");
    }
}
