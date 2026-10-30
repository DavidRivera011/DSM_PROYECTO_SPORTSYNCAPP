
@file:Suppress(
  "KotlinRedundantDiagnosticSuppress",
  "PropertyName",
  "MayBeConstant",
  "RedundantVisibilityModifier",
  "RedundantCompanionReference",
  "RemoveEmptyClassBody",
  "SpellCheckingInspection",
  "unused",
)

package com.example.sportsyncapp.connector



public interface CreateInstitucionMutation :
    com.google.firebase.dataconnect.generated.GeneratedMutation<
      DefaultConnector,
      CreateInstitucionMutation.Data,
      CreateInstitucionMutation.Variables
    >
{
  
    @kotlinx.serialization.Serializable
  public data class Variables(
  
    val nombre: String,
  
    val logo: com.google.firebase.dataconnect.OptionalVariable<String?>,
  
    val descripcion: com.google.firebase.dataconnect.OptionalVariable<String?>,
  
  ) {
    
    
      
      @kotlin.DslMarker public annotation class BuilderDsl

      
      @BuilderDsl
      public interface Builder {
        public var nombre: String
        public var logo: String?
        public var descripcion: String?
        
      }

      public companion object {
        
        @Suppress("NAME_SHADOWING")
        public fun build(
          nombre: String,
          block_: Builder.() -> Unit
        ): Variables {
          var nombre= nombre
            var logo: com.google.firebase.dataconnect.OptionalVariable<String?> =
                com.google.firebase.dataconnect.OptionalVariable.Undefined
            var descripcion: com.google.firebase.dataconnect.OptionalVariable<String?> =
                com.google.firebase.dataconnect.OptionalVariable.Undefined
            

          return object : Builder {
            override var nombre: String
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { nombre = value_ }
              
            override var logo: String?
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { logo = com.google.firebase.dataconnect.OptionalVariable.Value(value_) }
              
            override var descripcion: String?
              get() = throw UnsupportedOperationException("getting builder values is not supported")
              set(value_) { descripcion = com.google.firebase.dataconnect.OptionalVariable.Value(value_) }
              
            
          }.apply(block_)
          .let {
            Variables(
              nombre=nombre,logo=logo,descripcion=descripcion,
            )
          }
        }
      }
    
  }
  

  
    @kotlinx.serialization.Serializable
  public data class Data(
  
    val instituciones_insert: InstitucionesKey,
  
  ) {
    
    
  }
  

  public companion object {
    public val operationName: String = "CreateInstitucion"

    public val dataDeserializer: kotlinx.serialization.DeserializationStrategy<Data> =
      kotlinx.serialization.serializer()

    public val variablesSerializer: kotlinx.serialization.SerializationStrategy<Variables> =
      kotlinx.serialization.serializer()
  }
}

public fun CreateInstitucionMutation.ref(
  
    nombre: String,

  
    block_: CreateInstitucionMutation.Variables.Builder.() -> Unit = {}
  
): com.google.firebase.dataconnect.MutationRef<
    CreateInstitucionMutation.Data,
    CreateInstitucionMutation.Variables
  > =
  ref(
    
      CreateInstitucionMutation.Variables.build(
        nombre=nombre,
  
    block_
      )
    
  )

public suspend fun CreateInstitucionMutation.execute(

  
    
      nombre: String,

  
    block_: CreateInstitucionMutation.Variables.Builder.() -> Unit = {}

  ): com.google.firebase.dataconnect.MutationResult<
    CreateInstitucionMutation.Data,
    CreateInstitucionMutation.Variables
  > =
  ref(
    
      nombre=nombre,
  
    block_
    
  ).execute()


