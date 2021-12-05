package com.lounah.binaryReporter.internal.compatators.methods

import com.lounah.binaryReporter.BinaryCompatibilityViolation
import com.lounah.binaryReporter.BinaryCompatibilityViolation.MethodSignature
import com.lounah.binaryReporter.BinarySignatureComparator
import com.lounah.binaryReporter.ViolationRule
import com.lounah.binaryReporter.internal.compatators.classes.filters.ClassBecameFinal
import com.lounah.binaryReporter.internal.compatators.classes.filters.ClassNameChanged
import com.lounah.binaryReporter.internal.compatators.classes.filters.ClassVisibilityLessen
import com.lounah.binaryReporter.internal.compatators.classes.filters.ClassSupertypesChanged
import kotlinx.validation.api.ClassBinarySignature

public class MethodsSignatureComparator : BinarySignatureComparator {

    override val rules: Set<ViolationRule> = setOf(
        ClassNameChanged(),
        ClassVisibilityLessen(),
        ClassSupertypesChanged(),
        ClassBecameFinal()
    )

    override fun compare(
        oldClass: ClassBinarySignature?,
        newClass: ClassBinarySignature?
    ): Set<BinaryCompatibilityViolation> {
        return rules
            .asSequence()
            .filter { rule -> rule.matches(oldClass, newClass) }
            .map { rule -> MethodSignature(oldClass, newClass, rule.getDescription(oldClass, newClass)) }
            .toSet()
    }
}
