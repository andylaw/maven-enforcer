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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.maven.model.Profile;

public class MutuallyExclusiveProfilesSet {

    /**
     * The Set of profiles that are mutually exclusive
     */
    private final Set<String> profileNames = new HashSet<>();

    /**
     * A comma-separated list of the profiles in this mutually exclusive set
     */
    private String profilesString;

    /**
     * Do we require one of them to be set?
     */
    private boolean requireOne = false;

    public void setProfilesString(String profilesString) {
        this.profilesString = profilesString;
        this.profileNames.clear();
        for (String profile : profilesString.split(",")) {
            this.profileNames.add(profile.trim());
        }
    }

    public void setRequireOne(boolean b) {
        this.requireOne = b;
    }

    public MutuallyExclusiveProfilesSet() {}

    public boolean ruleIsSatisfied(List<Profile> activeProfileNames) {

        // This line is equivalent to a for loop counting how many of the profiles specified in
        // this rule are included in the supplied list of active profiles
        long countActives = activeProfileNames.stream()
                .map(Profile::getId)
                .filter(this.profileNames::contains)
                .count();
        // If we requireOne, then countActives must be 1. If we don't requireOne, then it must be <= 1
        return (countActives == 1 && this.requireOne) || (countActives <= 1 && !this.requireOne);
    }

    /**
     * @return rule description
     */
    @Override
    public String toString() {
        return String.format(
                "MutuallyExclusiveProfilesSet[oneRequired=%b, profileList=%s]", requireOne, this.profileNames);
    }
}
