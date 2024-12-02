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

import javax.inject.Inject;
import javax.inject.Named;

import java.util.ArrayList;
import java.util.List;

import org.apache.maven.enforcer.rule.api.AbstractEnforcerRule;
import org.apache.maven.enforcer.rule.api.EnforcerRuleException;
import org.apache.maven.execution.MavenSession;
import org.apache.maven.project.MavenProject;
import org.apache.maven.rtinfo.RuntimeInformation;

/**
 * Mutually Exclusive Profiles Enforcer Rule
 */
@Named("mutuallyExclusiveProfiles")
public class MutuallyExclusiveProfilesRule extends AbstractEnforcerRule {

    /**
     * List of Profiles specified in the configuration.
     */
    private List<MutuallyExclusiveProfilesSet> profilesRuleList;

    // Inject needed Maven components

    @Inject
    private MavenProject project;

    @Inject
    private MavenSession session;

    @Inject
    private RuntimeInformation runtimeInformation;

    public void execute() throws EnforcerRuleException {

        getLog().info("Retrieved Target Folder: " + project.getBuild().getDirectory());
        getLog().info("Retrieved ArtifactId: " + project.getArtifactId());
        getLog().info("Retrieved Project: " + project);
        getLog().info("Retrieved Maven version: " + runtimeInformation.getMavenVersion());
        getLog().info("Retrieved Session: " + session);
        getLog().info(() -> "Mutually Exclusive Profiles: " + profilesRuleList);
        getLog().info("Profiles Active: " + project.getActiveProfiles());

        // Check each of our rules
        List<String> failures = new ArrayList<>();
        // Fail if we have more than one of our specified profiles active
        for (MutuallyExclusiveProfilesSet profileSet : this.profilesRuleList) {

            getLog().info("Profile Set: " + profileSet);

            if (!profileSet.ruleIsSatisfied(project.getActiveProfiles())) {
                failures.add("  " + profileSet);
            }
        }

        if (!failures.isEmpty()) {
            String failureString = "The following Mutually Exclusive Profile Set rule(s) failed:"
                    + System.lineSeparator()
                    + String.join(System.lineSeparator(), failures)
                    + System.lineSeparator()
                    + "Profiles Active were: " + project.getActiveProfiles();
            throw new EnforcerRuleException(failureString);
        }
    }

    /**
     * @return rule description
     */
    @Override
    public String toString() {
        return String.format("MutuallyExclusiveProfiles[profilesRuleList=%s]", profilesRuleList);
    }
}
